/*
 *  Copyright 2015 Chiwan Park
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.chiwanpark.push.services

import akka.actor.Props
import com.chiwanpark.push.Configuration
import com.chiwanpark.push.actors.PushActor
import com.chiwanpark.push.actors.PushActor.{SendPushToAppleDevices, SendPushToAppleDevice}
import com.chiwanpark.push.database.CertificateConversion._
import com.chiwanpark.push.database.{Certificate, CertificateQuery}
import com.chiwanpark.push.util.Crypto
import com.chiwanpark.push.util.PushJsonProtocol._
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import spray.http.HttpResponse
import spray.json._

import scala.util.{Failure, Success}

trait PushService extends WebService with DatabaseService {
  val log = Logger(LoggerFactory.getLogger(classOf[PushService]))
  val pushActor = actorRefFactory.actorOf(Props[PushActor], "push-actor")

  def certificateQueryFromToken(token: String) = {
    val decodedToken = Crypto.decrypt(Crypto.decodeBase64(token), Configuration.SECRET_KEY)
    val decryptedToken = new String(decodedToken, "UTF-8").parseJson.convertTo[Map[String, Int]]

    CertificateQuery.selectById(decryptedToken("id"))
  }

  val pushMessageToSingleDevice = path("to-single-device") {
    post {
      formFields('token.as[String], 'message.as[String], 'badge.as[Int], 'device.as[String]) {
        case (token: String, message: String, badge: Int, device: String) =>
          val query = certificateQueryFromToken(token)
          val process = db.run(query).map { result: Seq[Certificate] =>
            if (result.length > 0) {
              val certificate = result.head
              pushActor ! SendPushToAppleDevice(certificate, message, badge, device)
            } else {
              throw new IllegalArgumentException("The given token is not valid.")
            }
          }

          onComplete(process) {
            case Success(_) => complete(HttpResponse(200))
            case Failure(e: IllegalArgumentException) => complete(HttpResponse(400))
            case Failure(e) => failWith(e)
          }
      }
    }
  }

  val pushMessageToMultipleDevices = path("to-multiple-devices") {
    post {
      formFields('token.as[String], 'message.as[String], 'badge.as[Int], 'devices.as[String]) {
        case (token: String, message: String, badge: Int, devices: String) =>
          val query = certificateQueryFromToken(token)
          val process = db.run(query).map { result: Seq[Certificate] =>
            if (result.length > 0) {
              val certificate = result.head
              val deviceTokens = devices.split(',')
              pushActor ! SendPushToAppleDevices(certificate, message, badge, deviceTokens)
            }
          }

          onComplete(process) {
            case Success(_) => complete(HttpResponse(200))
            case Failure(e: IllegalArgumentException) => complete(HttpResponse(400))
            case Failure(e) => failWith(e)
          }
      }
    }
  }

  val pushRoute = pathPrefix("push") {
    pushMessageToSingleDevice ~ pushMessageToMultipleDevices
  }
}

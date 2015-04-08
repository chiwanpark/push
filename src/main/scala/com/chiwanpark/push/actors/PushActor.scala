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

package com.chiwanpark.push.actors

import java.io.ByteArrayInputStream

import akka.actor.Actor
import com.chiwanpark.push.database.APNSCertificate
import com.chiwanpark.push.util.Crypto
import com.notnoop.apns.{ApnsService, APNS}
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

object PushActor {

  case class SendPushToAppleDevice(certificate: APNSCertificate, message: String, badge: Int, deviceToken: String)

  case class SendPushToAppleDevices(certificate: APNSCertificate, messages: String, badge: Int,
                                    deviceTokens: Array[String])

}

class PushActor extends Actor {

  import PushActor._

  implicit val system = context.system
  val log = Logger(LoggerFactory.getLogger(classOf[PushActor]))

  def apnsService(certificate: APNSCertificate) = {
    val p12Stream = new ByteArrayInputStream(Crypto.decodeBase64(certificate.certificate))
    val production = certificate.mode == "production"
    APNS.newService.withCert(p12Stream, certificate.password).withAppleDestination(production).build()
  }

  override def receive: Receive = {
    case SendPushToAppleDevice(certificate, message, badge, deviceToken) =>
      val service: ApnsService = apnsService(certificate)
      val payload = APNS.newPayload.badge(badge).alertBody(message).build()

      try {
        service.push(deviceToken, payload)
      } catch {
        case e: Exception => log.error("Push Message could not be sent.", e)
      } finally {
        service.stop()
      }
    case SendPushToAppleDevices(certificate, message, badge, deviceTokens) =>
      val service: ApnsService = apnsService(certificate)
      val payload = APNS.newPayload.badge(badge).alertBody(message).build()

      try {
        service.push(deviceTokens.toSeq, payload)
      } catch {
        case e: Exception => log.error("Push Message could not be sent.", e)
      } finally {
        service.stop()
      }
  }
}

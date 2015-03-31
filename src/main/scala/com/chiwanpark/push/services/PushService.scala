package com.chiwanpark.push.services

import akka.actor.Props
import com.chiwanpark.push.Configuration
import com.chiwanpark.push.actors.PushActor
import com.chiwanpark.push.actors.PushActor.SendPushToAppleDevice
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

  val pushMessageToSingleDevice = path("to-single-device") {
    post {
      formFields('token.as[String], 'message.as[String], 'badge.as[Int], 'device.as[String]) {
        case (token: String, message: String, badge: Int, device: String) =>
          val decodedToken = Crypto.decrypt(Crypto.decodeBase64(token), Configuration.SECRET_KEY)
          val decryptedToken = new String(decodedToken, "UTF-8").parseJson.convertTo[Map[String, Int]]
          val query = CertificateQuery.selectById(decryptedToken("id"))

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

  val pushRoute = pathPrefix("push") {
    pushMessageToSingleDevice
  }
}

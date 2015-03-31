package com.chiwanpark.push.actors

import java.io.ByteArrayInputStream

import akka.actor.Actor
import com.chiwanpark.push.database.APNSCertificate
import com.chiwanpark.push.util.Crypto
import com.notnoop.apns.APNS
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

object PushActor {

  case class SendPushToAppleDevice(certificate: APNSCertificate, message: String, badge: Int, deviceToken: String)

}

class PushActor extends Actor {

  import PushActor._

  implicit val system = context.system
  val log = Logger(LoggerFactory.getLogger(classOf[PushActor]))

  override def receive: Receive = {
    case SendPushToAppleDevice(certificate, message, badge, deviceToken) =>
      val p12Stream = new ByteArrayInputStream(Crypto.decodeBase64(certificate.certificate))
      val production = certificate.mode == "production"
      val apns = APNS.newService.withCert(p12Stream, certificate.password).withAppleDestination(production).build()
      val payload = APNS.newPayload.badge(badge).alertBody(message).build()

      try {
        apns.push(deviceToken, payload)
      } catch {
        case e: Exception => log.error("Push Message could not be sent.", e)
      }
  }
}

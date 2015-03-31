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

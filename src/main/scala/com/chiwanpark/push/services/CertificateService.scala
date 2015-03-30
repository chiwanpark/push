package com.chiwanpark.push.services

import com.chiwanpark.push.Configuration
import com.chiwanpark.push.database.{Certificate, CertificateQuery}
import com.chiwanpark.push.util.Crypto
import com.chiwanpark.push.util.PushJsonProtocol._
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import spray.http.HttpResponse
import spray.json._

import scala.util.{Failure, Success}


trait CertificateService extends WebService with DatabaseService {
  val logger = Logger(LoggerFactory.getLogger(classOf[CertificateService]))

  val getCertificate = path(IntNumber) { id =>
    get {
      val query = CertificateQuery.selectById(id)
      onComplete(db.run(query)) {
        case Success(results: Seq[Certificate]) =>
          if (results.length > 0) {
            complete(HttpResponse(entity = results.head.toJson.compactPrint))
          } else {
            complete(HttpResponse(404))
          }
        case Failure(e) => failWith(e)
      }
    }
  }

  val postAPNSCertificate = path("apns") {
    post {
      formFields('name.as[String], 'certificate.as[String], 'password.as[String]) {
        case (name: String, certificate: String, password: String) =>
          val value = Map("certificate" -> certificate, "password" -> password).toJson.compactPrint
          val query = CertificateQuery.insert("apns", name, value)

          onComplete(db.run(query)) {
            case Success(id: Int) =>
              val result = Map("id" -> id).toJson.compactPrint
              complete(HttpResponse(entity = result))
            case Failure(e) => failWith(e)
          }
      }
    }
  }
  
  val getToken = path(IntNumber / "token") { id =>
    get {
      val query = CertificateQuery.selectById(id)
      val process = db.run(query).map { result: Seq[Certificate] =>
        if (result.length > 0) {
          val certificate = result.head
          val json = Map("id" -> certificate.id).toJson.compactPrint
          val encrypted = Crypto.encrypt(json, Configuration.SECRET_KEY)
          Map("token" -> Crypto.encodeBase64(encrypted)).toJson.compactPrint
        } else {
          throw new IllegalArgumentException("Certificate ID is wrong")
        }
      }

      onComplete(process) {
        case Success(result: String) => complete(HttpResponse(entity = result))
        case Failure(e: IllegalArgumentException) => complete(HttpResponse(404))
        case Failure(e) => failWith(e)
      }
    }
  }

  val certificateRoute = pathPrefix("certificates") {
    getCertificate ~ postAPNSCertificate ~ getToken
  }
}

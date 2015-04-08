/*
 *  Copyright 2013 Chiwan Park
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

package com.chiwanpark.push.database

import com.chiwanpark.push.util.PushJsonProtocol._
import slick.driver.PostgresDriver.api._
import slick.lifted.Tag
import spray.json._

case class Certificate(id: Option[Int] = None, certType: String, name: String, value: String)

case class APNSCertificate(id: Option[Int] = None, name: String, mode: String, certificate: String, password: String)

object CertificateConversion {
  implicit def CertificateToAPNSCertificate(certificate: Certificate): APNSCertificate = certificate.certType match {
    case "apns" =>
      val value = certificate.value.parseJson.convertTo[Map[String, String]]
      APNSCertificate(certificate.id, certificate.name, value("mode"), value("certificate"), value("password"))
    case _ => throw new IllegalArgumentException("Cannot convert the given certificate to APNSCertificate!")
  }

  implicit def APNSCertificateToCertificate(certificate: APNSCertificate): Certificate = {
    val value = Map("mode" -> certificate.mode, "certificate" -> certificate.certificate,
      "password" -> certificate.password)
    Certificate(certificate.id, "apns", certificate.name, value.toJson.compactPrint)
  }
}

class CertificateTable(tag: Tag) extends Table[Certificate](tag, "push_certificates") {
  def id = column[Int]("CERTIFICATE_ID", O.PrimaryKey, O.AutoInc)
  def certType = column[String]("CERTIFICATE_TYPE")
  def name = column[String]("NAME")
  def value = column[String]("VALUE")

  override def * = (id.?, certType, name, value) <>(Certificate.tupled, Certificate.unapply)
}

object CertificateQuery extends TableQuery(new CertificateTable(_)) {
  def insert(certificiate: Certificate) = (this returning this.map(_.id)) += certificiate

  def selectById(id: Int) = this.filter(_.id === id).result
}

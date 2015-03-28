package com.chiwanpark.push.database

import slick.driver.PostgresDriver.api._
import slick.lifted.Tag

case class Certificate(id: Option[Int] = None, certType: String, name: String, value: String)

class CertificateTable(tag: Tag) extends Table[Certificate](tag, "certificates") {
  def id = column[Int]("CERTIFICATE_ID", O.PrimaryKey, O.AutoInc)
  def certType = column[String]("CERTIFICATE_TYPE")
  def name = column[String]("NAME")
  def value = column[String]("VALUE")

  override def * = (id.?, certType, name, value) <>(Certificate.tupled, Certificate.unapply)
}

object CertificateQuery extends TableQuery(new CertificateTable(_)) {
  def insert(certType: String, name: String, value: String) =
    (this returning this.map(_.id)) += Certificate(None, certType, name, value)

  def selectById(id: Int) = this.filter(_.id === id).result
}

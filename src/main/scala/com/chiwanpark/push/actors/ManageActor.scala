package com.chiwanpark.push.actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import com.chiwanpark.push.database.{CertificateQuery, UserQuery}
import com.chiwanpark.push.services.DatabaseService
import slick.driver.PostgresDriver.api._

object ManageActor {

  case object CreateTable

  case class CreateSuperUser(username: String, password: String)

}

class ManageActor extends Actor with ActorLogging with DatabaseService {

  import ManageActor._
  import context.dispatcher

  implicit val system = context.system

  override def receive: Receive = {
    case CreateTable =>
      val schema = CertificateQuery.schema ++ UserQuery.schema
      db.run(schema.create) pipeTo sender
    case CreateSuperUser(username, password) =>
      val query = UserQuery.insert(username, password)
      db.run(query) pipeTo sender
  }
}

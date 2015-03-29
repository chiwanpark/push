package com.chiwanpark.push.actors

import akka.actor.{Actor, ActorLogging}
import com.chiwanpark.push.database.CertificateQuery
import com.chiwanpark.push.services.DatabaseService
import slick.driver.PostgresDriver.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ManageActor {

  case object CreateTable

}

class ManageActor extends Actor with ActorLogging with DatabaseService {
  implicit val system = context.system

  import ManageActor._

  override def receive: Receive = {
    case CreateTable =>
      val schema = CertificateQuery.schema
      sender ! Await.result(db.run(schema.create), Duration.Inf)
  }
}

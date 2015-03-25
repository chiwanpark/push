package com.chiwanpark.push

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.chiwanpark.push.services.ServiceActor
import spray.can.Http

import scala.concurrent.duration._
import scala.util.Properties

object Main extends App {
  implicit val system = ActorSystem("push-system")
  implicit val timeout = Timeout(5.seconds)

  val service = system.actorOf(Props[ServiceActor], "push-service")
  val port = Properties.envOrElse("PORT", "8080").toInt

  IO(Http) ask Http.Bind(service, "0.0.0.0", port)
}

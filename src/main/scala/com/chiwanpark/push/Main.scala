package com.chiwanpark.push

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.chiwanpark.push.actors.ServiceActor
import spray.can.Http

import scala.concurrent.duration._

object Main extends App {
  implicit val system = ActorSystem("push-system")
  implicit val timeout = Timeout(5.seconds)

  val service = system.actorOf(Props[ServiceActor], "push-service")

  IO(Http) ask Http.Bind(service, "0.0.0.0", Configuration.PORT)
}

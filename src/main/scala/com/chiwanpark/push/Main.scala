package com.chiwanpark.push

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.chiwanpark.push.services.ServiceActor
import spray.can.Http

import scala.concurrent.duration._

object Main extends App {
  implicit val system = ActorSystem("push-system")
  implicit val timeout = Timeout(5.seconds)

  if (args.length > 0) {
    args(0) match {
      case "initdb" => // TODO: need implementation
      case _ =>
    }
  } else {
    val service = system.actorOf(Props[ServiceActor], "push-service")
    IO(Http) ask Http.Bind(service, "0.0.0.0", Configuration.PORT)
  }
}

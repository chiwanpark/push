/*
 *  Copyright 2015 Chiwan Park
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

package com.chiwanpark.push

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.chiwanpark.push.actors.ManageActor.{CreateSuperUser, CreateTable}
import com.chiwanpark.push.actors.{ManageActor, ServiceActor}
import spray.can.Http

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App {
  implicit val system = ActorSystem("push-system")
  implicit val timeout = Timeout(5.seconds)

  if (args.length > 0) {
    val service = system.actorOf(Props[ManageActor], "push-manage")
    args(0) match {
      case "initdb" => Await.result(service ? CreateTable, Duration.Inf)
      case "createsuperuser" if args.length == 3 =>
        Await.result(service ? CreateSuperUser(args(1), args(2)), Duration.Inf)
      case _ =>
    }

    system.shutdown()
  } else {
    val service = system.actorOf(Props[ServiceActor], "push-service")
    IO(Http) ask Http.Bind(service, "0.0.0.0", Configuration.PORT)
  }
}

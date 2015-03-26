package com.chiwanpark.push.services

import akka.util.Timeout
import spray.routing.HttpService

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

trait WebService extends HttpService {
  implicit def executionContext: ExecutionContextExecutor = actorRefFactory.dispatcher
  implicit val timeout = Timeout(120.seconds)
}

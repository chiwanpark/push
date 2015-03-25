package com.chiwanpark.push.services

import akka.actor.{Actor, ActorLogging, ActorRefFactory}

class ServiceActor extends Actor with ActorLogging with IndexService {
  implicit val system = context.system

  override implicit def actorRefFactory: ActorRefFactory = context

  override def receive: Receive = runRoute(indexRoute)
}

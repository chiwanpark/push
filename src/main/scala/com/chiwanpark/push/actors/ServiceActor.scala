package com.chiwanpark.push.actors

import akka.actor.{Actor, ActorRefFactory}
import com.chiwanpark.push.services.{CertificateService, IndexService}

class ServiceActor extends Actor with IndexService with CertificateService {
  implicit val system = context.system

  override implicit def actorRefFactory: ActorRefFactory = context

  override def receive: Receive = runRoute(indexRoute ~ certificateRoute)
}

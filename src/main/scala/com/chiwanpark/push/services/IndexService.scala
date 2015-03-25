package com.chiwanpark.push.services

import spray.routing.HttpService

trait IndexService extends HttpService {
  val indexRoute = path("") {
    getFromResource("pages/index.html")
  }
}

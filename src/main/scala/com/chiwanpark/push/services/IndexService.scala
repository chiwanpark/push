package com.chiwanpark.push.services

trait IndexService extends WebService {
  val indexRoute = path("") {
    getFromResource("pages/index.html")
  }
}

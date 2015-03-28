package com.chiwanpark.push.services

import java.net.URI

import com.chiwanpark.push.Configuration
import slick.driver.PostgresDriver.api._

trait DatabaseService {
  def db = {
    val uri = new URI(Configuration.DATABASE_URL)
    val Array(username, password) = uri.getUserInfo.split(":")
    val url = "jdbc:postgresql://" + uri.getHost + ":" + uri.getPort + uri.getPath
    Database.forURL(url, username, password, driver = "org.postgresql.Driver")
  }
}

package com.chiwanpark.push

import scala.util.Properties

object Configuration {
  def DATABASE_URL = Properties.envOrElse("DATABASE_URL", "")
  def PORT = Properties.envOrElse("PORT", "8080").toInt
  def SECRET_KEY = Properties.envOrElse("SECRET_KEY", "")
}

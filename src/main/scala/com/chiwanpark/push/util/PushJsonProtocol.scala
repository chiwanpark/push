package com.chiwanpark.push.util

import com.chiwanpark.push.database.Certificate
import spray.json.DefaultJsonProtocol

object PushJsonProtocol extends DefaultJsonProtocol {
  implicit val certificateFormat = jsonFormat4(Certificate)
}

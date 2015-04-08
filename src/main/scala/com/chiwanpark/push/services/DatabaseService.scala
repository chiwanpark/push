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

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

import scala.util.Properties

object Configuration {
  def DATABASE_URL = Properties.envOrElse("DATABASE_URL", "")
  def PORT = Properties.envOrElse("PORT", "8080").toInt
  def SECRET_KEY = Properties.envOrElse("SECRET_KEY", "")
  def DEBUG = Properties.envOrElse("DEBUG", "false").toLowerCase == "true"
}

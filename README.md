# push
Simple RESTful Push Notification Server

## Current Version

* Stable: 0.1.0
* Development: 0.2.0-SNAPSHOT

## Current Supported Features

* Send message to a Apple Device (iPhone, iPad)

## Requirements

* Scala 2.11
* Java 1.7
* PostgreSQL 9.3 or above
 
## Usage
Some environment variables to run this server are following:

* `DATABASE_URL`: Database URL (e.g. `postgres://foo:foo@heroku.com:5432/hellodb`)
* `SECRET_KEY`: Secret Key value to encrypt critical values. The key's length must be 16 or 32.

You can run server 

## License

Copyright 2015 Chiwan Park

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
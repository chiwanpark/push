package com.chiwanpark.push.database

import com.chiwanpark.push.Configuration
import com.chiwanpark.push.util.Crypto
import slick.driver.PostgresDriver.api._
import slick.lifted.Tag

case class User(id: Option[Int] = None, username: String, password: String)

class UserTable(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Int]("USER_ID", O.PrimaryKey, O.AutoInc)
  def username = column[String]("USERNAME")
  def password = column[String]("USER_PASSWORD")

  def usernameIdx = index("IDX_USERNAME", username, unique = true)

  override def * = (id.?, username, password) <>(User.tupled, User.unapply)
}

object UserQuery extends TableQuery(new UserTable(_)) {
  def insert(username: String, password: String) = {
    val hashedPassword = Crypto.hash(password, Configuration.SECRET_KEY)
    (this returning this.map(_.id)) += User(None, username, hashedPassword)
  }

  def selectById(id: Int) = this.filter(_.id === id).result

  def selectByUsername(username: String) = this.filter(_.username === username).result

  def selectByUsernameAndPassword(username: String, password: String) = {
    val hashedPassword = Crypto.hash(password, Configuration.SECRET_KEY)
    this.filter(_.username === username).filter(_.password === password).result
  }
}

package am.models.db

import am.models.User
import am.services.db._

trait UsersTable {
  this: DbComponent =>
  import dbConfig.driver.api._
  
  class Users(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Option[String]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def team = column[String]("team")

    def * = (id, name, team) <> (User.tupled, User.unapply)
  }

  val users = TableQuery[Users]
}

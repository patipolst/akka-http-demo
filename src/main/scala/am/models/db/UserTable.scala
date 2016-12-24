package am.models.db

import am.models.User
import am.services.impl.DatabaseService

trait UserTable {
  val databaseService: DatabaseService
  import databaseService.driver.api._

  class Users(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Option[String]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def books = column[String]("books")

    def * = (id, name, books) <> (User.tupled, User.unapply)
  }

  val users = TableQuery[Users]
}

package am.models.db

import am.models.User
import am.services.impl.DatabaseService

trait UserEntity {

  protected val databaseService: DatabaseService
  import databaseService.driver.api._

  class UserTable(tag: Tag) extends Table[User](tag, "user") {
    def id = column[String]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def books = column[String]("books")

    def * = (id, name, books) <> (User.tupled, User.unapply)
  }

  protected val user = TableQuery[UserTable]

}

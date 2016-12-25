package am.models.db

import am.models.Book
import am.services.db.DatabaseService

trait BooksTable extends UsersTable {
  val databaseService: DatabaseService
  import databaseService.driver.api._

  class Books(tag: Tag) extends Table[Book](tag, "books") {
    def id = column[Option[String]]("id", O.PrimaryKey, O.AutoInc)
    def userID = column[Option[String]]("user_id")
    def name = column[String]("name")

    def * = (id, userID, name) <> (Book.tupled, Book.unapply)

    def user = foreignKey("user_fk", userID, users)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  }

  val books = TableQuery[Books]
}

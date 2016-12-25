package am.services

import am.models._
import am.models.db.BooksTable
import am.services.db._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class BooksService(val databaseService: DatabaseService) extends BooksTable {
  import databaseService._
  import databaseService.driver.api._

  def createBooksTable: Unit = db.run(DBIO.seq((books.schema).create))

  def getBooks(): Future[Seq[Book]] = db.run(books.result)

  def getBookById(id: String): Future[Option[Book]] =
    db.run(books.filter(_.id === id).result.headOption)

  def createBook(user: Book): Future[Book] =
    db.run((books returning books.map(_.id)
      into ((user, id) => user.copy(id = id))) += user)

  def updateBook(id: String, userUpdate: BookUpdate): Future[Option[Book]] =
    getBookById(id).flatMap {
      case Some(foundBook) =>
        val updatedBook = userUpdate.merge(foundBook)
        db.run(books.filter(_.id === id).update(updatedBook)).map(_ => Some(updatedBook))
      case None => Future.successful(None)
    }

  def deleteBook(id: String): Future[Int] = db.run(books.filter(_.id === id).delete)
}

package am.api.routes

import am.services.BooksService
import am.models._
import am.utils.Helper
import scala.util.{ Success, Failure }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes.{ Success => HttpSuccess }
import akka.http.scaladsl.model.StatusCodes._
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.Json

class BooksServiceRoute(booksService: BooksService) extends Helper {
  import booksService._

  val requiredFields = List("id", "userID", "name")

  val route = pathPrefix("books") {
    pathEndOrSingleSlash {
      get {
        onComplete(getBooks) {
          case Success(books) => books match {
            case Nil => complete(errorResponse(s"Books $NOT_FOUND", NotFound))
            case books => complete(dataResponse(books.asJson, OK))
          }
          case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
        }
      } ~
      (post & entity(as[Json])) { json =>
        json.as[Book] match {
          case Right(book) => onComplete(createBook(book)) {
            case Success(createdBook) => complete(dataResponse(createdBook.asJson, Created))
            case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
          }
          case Left(error) => complete(errorResponse(checkFields(json, requiredFields), BadRequest))
        }
      }
    } ~
    pathPrefix(Segment) { id =>
      pathEndOrSingleSlash {
        get {
          onComplete(getBookById(id)) {
            case Success(books) => books match {
              case Some(book) => complete(dataResponse(book.asJson, OK))
              case None => complete(errorResponse(s"Book ID: $id $NOT_FOUND", NotFound))
            }
            case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
          }
        } ~
        (put & entity(as[Json])) { json =>
          json.as[BookUpdate] match {
            case Right(book) => onComplete (updateBook(id, book)) {
              case Success(result) => result match {
                case Some(book) => complete(dataResponse(book.asJson, OK))
                case None => complete(errorResponse(s"Book ID: $id $CANNOT_UPDATE", BadRequest))
              }
              case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
            }
            case Left(error) => complete(errorResponse(checkFields(json, requiredFields), BadRequest))
          }
        } ~
        delete {
          onComplete(deleteBook(id)) {
            case Success(result) => result match {
              case 1 => complete(dataResponse(s"Book ID: $id $DELETED".asJson, OK))
              case 0 => complete(errorResponse(s"Book ID: $id $CANNOT_DELETE", BadRequest))
            }
            case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
          }
        }
      }
    }
  }
}

package am.api.routes

import am.services.UsersService
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

class UsersServiceRoute(usersService: UsersService) extends Helper {
  import usersService._

  val requiredFields = List("name", "age", "addressId")

  val route = pathPrefix("users") {
    pathEndOrSingleSlash {
      (get & parameter("address".as[Boolean]?)) { queryAddress =>
        queryAddress match {
          case Some(true) => onComplete(getUsersWithAddress) {
            case Success(users) => users match {
              case Nil => complete(errorResponse(s"Users $NOT_FOUND", NotFound))
              case users => complete(dataResponse(users.asJson, OK))
            }
            case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
          }
          case _ => onComplete(getUsers) {
            case Success(users) => users match {
              case Nil => complete(errorResponse(s"Users $NOT_FOUND", NotFound))
              case users => complete(dataResponse(users.asJson, OK))
            }
            case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
          }
        }
      } ~
      (post & entity(as[Json])) { json =>
        json.as[User] match {
          case Right(user) => onComplete(createUser(user)) {
            case Success(createdUser) => complete(dataResponse(createdUser.asJson, Created))
            case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
          }
          case Left(error) => complete(errorResponse(checkFields(json, requiredFields), BadRequest))
        }
      }
    } ~
    pathPrefix(Segment) { id =>
      pathEndOrSingleSlash {
        (get & parameter("address".as[Boolean]?)) { queryAddress =>
          queryAddress match {
            case Some(true) => onComplete(getUserWithAddressById(id)) {
              case Success(users) => users match {
                case Some(user) => complete(dataResponse(user.asJson, OK))
                case None => complete(errorResponse(s"User ID: $id $NOT_FOUND", NotFound))
              }
              case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
            }
            case _ => onComplete(getUserById(id)) {
              case Success(users) => users match {
                case Some(user) => complete(dataResponse(user.asJson, OK))
                case None => complete(errorResponse(s"User ID: $id $NOT_FOUND", NotFound))
              }
              case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
            }
          }
        } ~
        (put & entity(as[Json])) { json =>
          json.as[UserUpdate] match {
            case Right(user) => onComplete (updateUser(id, user)) {
              case Success(result) => result match {
                case Some(user) => complete(dataResponse(user.asJson, OK))
                case None => complete(errorResponse(s"User ID: $id $CANNOT_UPDATE", BadRequest))
              }
              case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
            }
            case Left(error) => complete(errorResponse(checkFields(json, requiredFields), BadRequest))
          }
        } ~
        delete {
          onComplete(deleteUser(id)) {
            case Success(result) => result match {
              case 1 => complete(dataResponse(s"User ID: $id $DELETED".asJson, OK))
              case 0 => complete(errorResponse(s"User ID: $id $CANNOT_DELETE", BadRequest))
            }
            case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
          }
        }
      }
    }
  }
}

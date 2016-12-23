package am.api.routes

import am.services.impl.UsersService
import am.models.User
import am.utils.Helper
import scala.util.{ Success => FutureSuccess }
import scala.util.Failure
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.Json

class UsersServiceRoute(usersService: UsersService) extends Helper {
  import usersService._

  val requiredFields = List("id", "name", "books")

  val route = pathPrefix("users") {
    pathEndOrSingleSlash {
      get {
        onComplete(getUsers) { 
          case FutureSuccess(Right(Nil)) => complete(errorResponse("Not found" :: Nil, NotFound))
          case FutureSuccess(Right(users)) => complete(dataResponse(users.asJson, OK))
          case FutureSuccess(Left(error)) => complete(errorResponse(error, InternalServerError))
          case Failure(error) => complete(errorResponse(error.toString :: Nil, InternalServerError))
        }
      } ~
      (post & entity(as[Json])) { json =>
        complete {
          json.as[User] match {
            case Right(user) => createUser(user) match {
              case Right(createdUser) => dataResponse(createdUser.asJson, Created)
              case Left(error) => errorResponse(error, InternalServerError)
            }
            case Left(error) => errorResponse(checkFields(json, requiredFields), BadRequest)
          }
        }
      }
    } ~
    pathPrefix(Segment) { id =>
      pathEndOrSingleSlash {
        (put & entity(as[Json])) { json =>
          complete {
            json.as[User] match {
              case Right(user) => updateUser(id, user) match {
                case Right(updatedUser) => dataResponse(updatedUser.asJson, Created)
                case Left(error) => errorResponse(error, BadRequest)
              }
              case Left(error) => errorResponse(checkFields(json, requiredFields), BadRequest)
            }
          }
        } ~
        delete {
          complete {
            deleteUser(id) match {
              case Right(users) => dataResponse(users.asJson, OK)
              case Left(error) => errorResponse(error, InternalServerError)
            }
          }
        }
      }
    }
  }
}

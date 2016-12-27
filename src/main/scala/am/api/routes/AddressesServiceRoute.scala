package am.api.routes

import am.services.AddressesService
import am.models._
import am.utils.{ Helper, Validator }
import scala.util.{ Success, Failure }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes.{ Success => HttpSuccess }
import akka.http.scaladsl.model.StatusCodes._
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.Json

class AddressesServiceRoute(addressesService: AddressesService) extends Helper {
  import addressesService._
  import Validator.addressValidator

  val requiredFields = List("street", "city")

  val route = pathPrefix("addresses") {
    pathEndOrSingleSlash {
      get {
        onComplete(getAddresses) {
          case Success(addresses) => addresses match {
            case Nil => complete(errorResponse(s"Addresses $NOT_FOUND", NotFound))
            case addresses => complete(dataResponse(addresses.asJson, OK))
          }
          case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
        }
      } ~
      (post & entity(as[Json])) { json =>
        normalize(json).as[Address] match {
          case Right(address) => validateModel(address) match {
            case Nil => onComplete(createAddress(address)) {
              case Success(createdAddress) => complete(dataResponse(createdAddress.asJson, Created))
              case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
            }
            case errors => complete(errorResponse(errors, BadRequest))
          }
          case Left(error) => complete(errorResponse(checkFields(json, requiredFields), BadRequest))
        }
      }
    } ~
    pathPrefix(Segment) { id =>
      pathEndOrSingleSlash {
        get {
          onComplete(getAddressById(id)) {
            case Success(addresses) => addresses match {
              case Some(address) => complete(dataResponse(address.asJson, OK))
              case None => complete(errorResponse(s"Address ID: $id $NOT_FOUND", NotFound))
            }
            case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
          }
        } ~
        (put & entity(as[Json])) { json =>
          normalize(json).as[AddressUpdate] match {
            case Right(address) => onComplete (updateAddress(id, address)) {
              case Success(result) => result match {
                case Some(address) => complete(dataResponse(address.asJson, OK))
                case None => complete(errorResponse(s"Address ID: $id $CANNOT_UPDATE", BadRequest))
              }
              case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
            }
            case Left(error) => complete(errorResponse(checkFields(json, requiredFields), BadRequest))
          }
        } ~
        delete {
          onComplete(deleteAddress(id)) {
            case Success(result) => result match {
              case 1 => complete(dataResponse(s"Address ID: $id $DELETED".asJson, OK))
              case 0 => complete(errorResponse(s"Address ID: $id $CANNOT_DELETE", BadRequest))
            }
            case Failure(error) => complete(errorResponse(DATABSE_EXCEPTION, InternalServerError))
          }
        }
      }
    }
  }
}

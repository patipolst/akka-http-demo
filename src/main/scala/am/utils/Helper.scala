package am.utils

import am.models.formats._
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, HttpResponse, StatusCode }
import io.circe.Json
import io.circe.generic.auto._
import io.circe.syntax._
import com.wix.accord._
import com.wix.accord.Descriptions.{ AccessChain, Generic }

object Helper extends Helper
trait Helper {

  val DELETED = "has been deleted"
  val NOT_FOUND = "not found"
  val CANNOT_UPDATE = "cannot be updated"
  val CANNOT_DELETE = "cannot be deleted"
  val DATABSE_EXCEPTION = "Database exception has occured"

  def checkFields(json: Json, reqFields: List[String]): List[String] = {
    val jsonFields = json.hcursor.fields.getOrElse(Nil)
    reqFields.diff(jsonFields).map(f => s"$f is required") match {
      case Nil => "Malformed JSON" :: Nil
      case errors => errors
    }
  }

  def normalize(json: Json): Json = {
    val trimString: PartialFunction[Json, Json] = {
      case json => json.mapString(_.trim)
    }
    val transform: PartialFunction[Json, Json] = trimString

    val jsonFields = json.hcursor.fields.getOrElse(Nil)
    jsonFields.foldLeft(json)((acc, field) =>
      acc.hcursor.downField(field).withFocus(transform).top match {
        case Some(result) => result
        case None => json
      }
    )
  }

  def validateModel[T](model: T)(implicit validator: Validator[T]): List[String] =
    validate(model) match {
      case Success => Nil
      case Failure(e) => e.map(x => x.description match {
        case AccessChain(Generic(field)) => s"$field: ${x.constraint}"
      }).toList
    }

  def plainTextResponse(text: String): HttpResponse =
    HttpResponse(entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, text))

  def dataResponse(json: Json, status: StatusCode): HttpResponse = {
    val dataResp = DataResponseFormat(json, status.intValue, status.defaultMessage)
    HttpResponse(status, entity = HttpEntity(ContentTypes.`application/json`, dataResp.asJson.toString))
  }

  def errorResponse(error: String, status: StatusCode): HttpResponse =
    errorResponse(error :: Nil, status)

  def errorResponse(errors: List[String], status: StatusCode): HttpResponse = {
    val errorResp = ErrorResponseFormat(errors, status.intValue, status.defaultMessage)
    HttpResponse(status, entity = HttpEntity(ContentTypes.`application/json`, errorResp.asJson.toString))
  }
}

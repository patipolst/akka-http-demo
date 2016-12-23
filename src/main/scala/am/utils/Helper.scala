package am.utils

import am.models.formats._
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, HttpResponse, StatusCode }
import io.circe.Json
import io.circe.generic.auto._
import io.circe.syntax._

object Helper extends Helper
trait Helper {
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

  def checkFields(json: Json, reqFields: List[String]): List[String] = {
    val jsonFields = json.hcursor.fields.getOrElse(Nil)
    reqFields.diff(jsonFields).map(f => s"$f is required") match {
      case Nil => "Malformed JSON" :: Nil
      case errors => errors
    }
  }

  def dataResponse(json: Json, status: StatusCode): HttpResponse = {
    val dataResp = DataResponseFormat(json, status.intValue, status.defaultMessage)
    HttpResponse(status, entity = HttpEntity(ContentTypes.`application/json`, dataResp.asJson.toString))
  }

  def errorResponse(errors: List[String], status: StatusCode): HttpResponse = {
    val errorResp = ErrorResponseFormat(errors, status.intValue, status.defaultMessage)
    HttpResponse(status, entity = HttpEntity(ContentTypes.`application/json`, errorResp.asJson.toString))
  }
}

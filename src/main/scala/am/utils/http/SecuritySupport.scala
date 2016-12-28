package am.utils.http

import am.utils.{ Config, Helper }
import scala.util.{ Success, Failure }
import akka.http.scaladsl.model.StatusCodes.Unauthorized
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives._
import pdi.jwt.{JwtCirce, JwtClaim, JwtAlgorithm}

trait SecuritySupport extends Config with Helper {
  def jwtAuthenticate: Directive1[JwtClaim] = {
    optionalHeaderValueByName("Authorization").flatMap {
      case Some(token) =>
        JwtCirce.decode(token, jwtKey, Seq(JwtAlgorithm.HS256)) match {
          case Success(jwtClaim) => provide(jwtClaim)
          case Failure(e) => complete(errorResponse(INVALID_TOKEN, Unauthorized))
        }
      case _ => complete(errorResponse(MISSING_TOKEN, Unauthorized))
    }
  }
}

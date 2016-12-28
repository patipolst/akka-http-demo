package am.utils.http

import akka.http.scaladsl.model.HttpMethods.{ GET, POST, PUT, DELETE, OPTIONS }
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ Directive0, Route }

trait CorsSupport {
  private def addAccessControlHeaders: Directive0 = {
    mapResponseHeaders { headers =>
      `Access-Control-Allow-Origin`.* +:
      `Access-Control-Allow-Credentials`(true) +:
      `Access-Control-Allow-Headers`("Authorization", "Content-Type", "X-Requested-With") +:
      headers
    }
  }

  private def preflightRequestHandler: Route = options {
    complete(HttpResponse(200).withHeaders(
      `Access-Control-Allow-Methods`(GET, POST, PUT, DELETE, OPTIONS)
    ))
  }

  def corsHandler(route: Route) = addAccessControlHeaders {
    preflightRequestHandler ~ route
  }
}

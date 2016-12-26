package am.api.routes

import am.utils.Helper
import akka.http.scaladsl.server.Directives._

object PingRoute extends Helper {
  val route = pathPrefix("ping") {
    pathEndOrSingleSlash {
      get {
        complete(plainTextResponse("Success"))
      }
    }
  }
}

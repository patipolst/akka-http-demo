package am.api.routes

import am.utils._
import akka.http.scaladsl.server.Directives._
import pdi.jwt.{JwtCirce, JwtClaim, JwtAlgorithm}

object TokenRoute extends Config with Helper {
  val route = pathPrefix("token") {
    pathEndOrSingleSlash {
      get {
        val jwtClaim = JwtClaim("""{"name": "test"}""")
        val jwtToken = JwtCirce.encode(jwtClaim, jwtKey, JwtAlgorithm.HS256)
        complete(plainTextResponse(jwtToken))
      }
    }
  }
}

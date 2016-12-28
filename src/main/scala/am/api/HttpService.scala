package am.api

import am.api.routes._
import am.services._
import am.utils.http.CorsSupport
import akka.http.scaladsl.server.Directives._

class HttpService(usersService: UsersService, addressesService: AddressesService)
  extends CorsSupport {

  val usersRouter = new UsersServiceRoute(usersService)
  val addressesRouter = new AddressesServiceRoute(addressesService)

  val routes = corsHandler {
    PingRoute.route ~
    pathPrefix("api" / "v1") {
      usersRouter.route ~
      addressesRouter.route
    }
  }

}

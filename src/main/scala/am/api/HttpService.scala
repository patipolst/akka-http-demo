package am.api

import am.api.routes.UsersServiceRoute
import am.services.impl.UsersService
import akka.http.scaladsl.server.Directives._

class HttpService(usersService: UsersService) {

  val usersRouter = new UsersServiceRoute(usersService)
  val routes =
    pathPrefix("api" / "v1") {
      usersRouter.route
    }

}

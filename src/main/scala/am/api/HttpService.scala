package am.api

import am.api.routes._
import am.services._
import akka.http.scaladsl.server.Directives._

class HttpService(usersService: UsersService) {
  val usersRouter = new UsersServiceRoute(usersService)
  // val booksRouter = new BooksServiceRoute(booksService)

  val routes =
    pathPrefix("api" / "v1") {
      usersRouter.route
      // ~booksRouter.route
    }
}

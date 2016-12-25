package am.api

import am.api.routes.UsersServiceRoute
import am.api.routes.BooksServiceRoute
import am.services.UsersService
import am.services.BooksService
import akka.http.scaladsl.server.Directives._

class HttpService(usersService: UsersService, booksService: BooksService) {
  val usersRouter = new UsersServiceRoute(usersService)
  val booksRouter = new BooksServiceRoute(booksService)

  val routes =
    pathPrefix("api" / "v1") {
      usersRouter.route ~
      booksRouter.route
    }
}

package am

import am.api.HttpService
import am.services._
import am.services.db._
import am.utils.Config
import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

object Main extends App with Config {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val logger = Logging(system, getClass)
  implicit val materializer = ActorMaterializer()

  val usersService = new UsersService(dbConfig)
  // val booksService = new BooksService(databaseService)
  usersService.createUsersTable
  // booksService.createBooksTable
  val httpService = new HttpService(usersService)

  Http().bindAndHandle(httpService.routes, httpHost, httpPort)
}

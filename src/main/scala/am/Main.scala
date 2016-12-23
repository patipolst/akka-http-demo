package am

import am.api.HttpService
import am.services.impl._
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

  val databaseService = new DatabaseService(jdbcUrl, dbUser, dbPassword)
  val usersService = new UsersService(databaseService)
  val httpService = new HttpService(usersService)

  Http().bindAndHandle(httpService.routes, httpHost, httpPort)
}

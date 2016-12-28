package am

import am.api.HttpService
import am.services._
import am.utils.Config
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

object Main extends App with Config {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executor = system.dispatcher

  val addressesService = new AddressesService(dbConfig)
  val usersService = new UsersService(dbConfig)
  val httpService = new HttpService(usersService, addressesService)

  Http().bindAndHandle(httpService.routes, httpHost, httpPort)
}

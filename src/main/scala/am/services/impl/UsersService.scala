package am.services.impl

import am.services.UsersServiceLike
import am.models.User
import am.models.db.UserEntity
import am.utils.Helper
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Success, Failure }
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.Json
import io.circe.parser._

class UsersService(val databaseService: DatabaseService) extends UsersServiceLike with UserEntity with Helper {
  import databaseService._
  import databaseService.driver.api._

  def getUsers() =  {
    // db.run(user.result)
    db.run(user.result.asTry) map {
      case Success(_) => Left(List())
    }
  }
  // flatMap {
  //   case Success(result) => Right(result)
  //   case Failure(error) => Left(error :: Nil)
  // }

  def getUserById(id: String): Either[List[String], User] = ???

  def createUser(user: User): Either[List[String], User] = {
    Right(User("123", "name", "b1"))
    // Right(User("123", "name", List("b1", "b2")))
  }

  def updateUser(id: String, user: User): Either[List[String], User] = {
    Right(User("123", "name", "b1"))
  }

  def deleteUser(id: String): Either[List[String], User] = ???
}

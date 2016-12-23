package am.services

import scala.concurrent.Future
import am.models.User

trait UsersServiceLike {
  def getUsers(): Future[Either[List[String], List[User]]]
  // def getUsers(): Either[List[String], List[User]]
  def getUserById(id: String): Either[List[String], User]
  def createUser(user: User): Either[List[String], User]
  def updateUser(id: String, user: User): Either[List[String], User]
  def deleteUser(id: String): Either[List[String], User]
}

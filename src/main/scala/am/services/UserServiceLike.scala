package am.services

import scala.concurrent.Future
import am.models._

trait UsersServiceLike {
  def getUsers(): Future[Seq[User]]
  def getUserById(id: String): Future[Option[User]]
  def createUser(user: User): Future[User]
  def updateUser(id: String, userUpdate: UserUpdate): Future[Option[User]]
  def deleteUser(id: String): Future[Int]
}

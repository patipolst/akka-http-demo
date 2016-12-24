package am.services.impl

import am.services.UsersServiceLike
import am.models._
import am.models.db.UserTable
import am.utils.Helper
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UsersService(val databaseService: DatabaseService) extends UsersServiceLike with UserTable with Helper {
  import databaseService._
  import databaseService.driver.api._

  def createUserTable: Unit = db.run(DBIO.seq((users.schema).create))

  def getUsers(): Future[Seq[User]] = db.run(users.result)

  def getUserById(id: String): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)

  def createUser(user: User): Future[User] =
    db.run((users returning users.map(_.id)
      into ((user, id) => user.copy(id = id))) += user)

  def updateUser(id: String, userUpdate: UserUpdate): Future[Option[User]] =
    getUserById(id).flatMap {
      case Some(foundUser) =>
        val updatedUser = userUpdate.merge(foundUser)
        db.run(users.filter(_.id === id).update(updatedUser)).map(_ => Some(updatedUser))
      case None => Future.successful(None)
    }

  def deleteUser(id: String): Future[Int] = db.run(users.filter(_.id === id).delete)
}

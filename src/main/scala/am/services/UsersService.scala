package am.services

import am.models._
import am.models.tables.UsersTable
import am.services.db._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

class UsersService(val dbConfig: DatabaseConfig[JdbcProfile]) extends UsersTable with DbComponent {
  import dbConfig.driver.api._

  def createUsersTable: Unit = db.run(DBIO.seq((users.schema).create,
    users ++= Seq(
      User(None, "Boom", 22, "1"),
      User(None, "Siggy", 40, "2"),
      User(None, "Yok", 27, "2")
    ))
  )

  def dropUsersTable: Unit = db.run(DBIO.seq((users.schema).drop))

  def getUsers(): Future[Seq[User]] = {
    db.run(users.result)
  }

  def getUsersWithAddress(): Future[Seq[UserWithAddress]] = {
    db.run(users.withAddress.result).map(_.map(UserWithAddress.tupled))
  }

  def getUserById(id: String): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)

  def getUserWithAddressById(id: String): Future[Option[UserWithAddress]] =
    db.run(users.withAddress.result.headOption).map(_.map(UserWithAddress.tupled))

  def createUser(newUser: User): Future[User] =
    db.run((users returning users.map(_.id)
      into ((user, id) => user.copy(id = Some(id)))) += newUser)

  def updateUser(id: String, userUpdate: UserUpdate): Future[Option[User]] =
    getUserById(id).flatMap {
      case Some(foundUser) =>
        val updatedUser = userUpdate.merge(foundUser)
        db.run(users.filter(_.id === id).update(updatedUser)).map(_ => Some(updatedUser))
      case None => Future.successful(None)
    }

  def deleteUser(id: String): Future[Int] = db.run(users.filter(_.id === id).delete)
}

package am.services

import am.models._
import am.models.tables.AddressesTable
import am.services.db._
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.meta.MTable

class AddressesService(val dbConfig: DatabaseConfig[JdbcProfile]) extends AddressesTable with DbComponent {
  import dbConfig.driver.api._

  def initializeTable: Unit = {
    val tables = List(addresses)
    val existingTables = db.run(MTable.getTables)
    val init = existingTables.flatMap( existing => {
      val names = existing.map(mt => mt.name.name)
      val createIfNotExist = tables.filter( table =>
        (!names.contains(table.baseTableRow.tableName))).map(_.schema.create)
      db.run(DBIO.sequence(createIfNotExist))
    })
    Await.result(init, Duration.Inf)

    db.run(
      addresses ++= Seq(
        Address(None, "Sukhumvit", "Bangkok"),
        Address(None, "Nimman", "Chiangmai"),
        Address(None, "Nimman", "Chiangmai")
      )
    )
  }

  def dropTable: Unit = db.run(DBIO.seq((addresses.schema).drop))

  def getAddresses(): Future[Seq[Address]] = db.run(addresses.result)

  def getAddressById(id: String): Future[Option[Address]] =
    db.run(addresses.filter(_.id === id).result.headOption)

  def createAddress(newAddress: Address): Future[Address] =
    db.run((addresses returning addresses.map(_.id)
      into ((address, id) => address.copy(id = Some(id)))) += newAddress)

  def updateAddress(id: String, addressUpdate: AddressUpdate): Future[Option[Address]] =
    getAddressById(id).flatMap {
      case Some(foundAddress) =>
        val updatedAddress = addressUpdate.merge(foundAddress)
        db.run(addresses.filter(_.id === id).update(updatedAddress)).map(_ => Some(updatedAddress))
      case None => Future.successful(None)
    }

  def deleteAddress(id: String): Future[Int] = db.run(addresses.filter(_.id === id).delete)
}

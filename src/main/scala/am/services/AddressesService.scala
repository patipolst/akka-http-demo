package am.services

import am.models._
import am.models.tables.AddressesTable
import am.services.db._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

class AddressesService(val dbConfig: DatabaseConfig[JdbcProfile]) extends AddressesTable with DbComponent {
  import dbConfig.driver.api._

  def createAddressesTable: Unit = db.run(DBIO.seq((addresses.schema).create,
    addresses ++= Seq(
      Address(None, "Sukhumvit", "Bangkok"),
      Address(None, "Nimman", "Chiangmai"),
      Address(None, "Nimman", "Chiangmai")
    ))
  )

  def dropAddressesTable: Unit = db.run(DBIO.seq((addresses.schema).drop))

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

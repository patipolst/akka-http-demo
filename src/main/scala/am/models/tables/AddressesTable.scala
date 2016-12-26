package am.models.tables

import am.models.Address
import am.services.db._

trait AddressesTable {
  this: DbComponent =>
  import dbConfig.driver.api._

  class Addresses(tag: Tag) extends Table[Address](tag, "addresses") {
    def id = column[String]("id", O.PrimaryKey, O.AutoInc)
    def street = column[String]("street")
    def city = column[String]("city")
    def * = (id.?, street, city) <> (Address.tupled, Address.unapply)
  }

  val addresses = TableQuery[Addresses]
}

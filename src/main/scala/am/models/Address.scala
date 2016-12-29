package am.models

case class Address(
  id: Option[String] = None,
  street: String,
  city: String
)

case class AddressUpdate(street: Option[String] = None, city: Option[String] = None) {
  def merge(address: Address): Address =
    address.copy(street = street.getOrElse(address.street), city = city.getOrElse(address.city))
}

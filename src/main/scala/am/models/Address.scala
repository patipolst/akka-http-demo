package am.models

case class Address(
  id: Option[String] = None,
  street: String,
  city: String
)

case class AddressUpdate(
  street: Option[String] = None,
  city: Option[String] = None
) {
  def merge(oldAddress: Address): Address =
    oldAddress.copy(street = street.getOrElse(oldAddress.street),
    city = city.getOrElse(oldAddress.city))
}

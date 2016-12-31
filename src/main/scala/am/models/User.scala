package am.models

case class User(
  id: Option[String] = None,
  name: String,
  age: Int,
  addressId: String
)

case class UserUpdate(
  name: Option[String] = None,
  age: Option[Int] = None,
  addressId: Option[String] = None
) {
  def merge(oldUser: User): User =
    oldUser.copy(name = name.getOrElse(oldUser.name),
    age = age.getOrElse(oldUser.age),
    addressId = addressId.getOrElse(oldUser.addressId))
}

case class UserWithAddress(
  user: User,
  address: Address
)

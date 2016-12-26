package am.models

case class User(
  id: Option[String] = None,
  name: String,
  age: Int,
  addressId: String
)

case class UserUpdate(name: Option[String] = None, age: Option[Int] = None, addressId: Option[String] = None) {
  def merge(user: User): User =
    user.copy(name = name.getOrElse(user.name), age = age.getOrElse(user.age), addressId = addressId.getOrElse(user.addressId))
}

case class UserWithAddress(
  user: User,
  address: Address
)

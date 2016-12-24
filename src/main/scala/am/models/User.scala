package am.models

case class User(
  id: Option[String] = None,
  name: String,
  books: String
  // books: List[String]
)

case class UserUpdate(name: Option[String] = None, books: Option[String] = None) {
  def merge(user: User): User = {
    User(user.id, name.getOrElse(user.name), books.getOrElse(user.books))
  }
}

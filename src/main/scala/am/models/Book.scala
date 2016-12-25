package am.models

case class Book(
  id: Option[String] = None,
  userID: Option[String] = None,
  name: String
)

case class BookUpdate(userID: Option[String] = None, name: Option[String] = None) {
  def merge(book: Book): Book =
    book.copy(userID = book.userID, name = name.getOrElse(book.name))
}

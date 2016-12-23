// package am.models.db
//
// import am.models.Book
// import am.services.impl.DatabaseService
//
// trait BookEntity {
//
//   protected val databaseService: DatabaseService
//   import databaseService.driver.api._
//
//   class BookTable(tag: Tag) extends Table[Book](tag, "users") {
//     def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
//     def username = column[String]("username")
//     def password = column[String]("password")
//
//     def userFk = foreignKey("USER_FK", userId, users)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
//
//     def * = (id, username, password) <> (Book.tupled, Book.unapply)
//   }
//
//   protected val users = TableQuery[BookTable]
//
// }

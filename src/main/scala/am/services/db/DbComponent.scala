package am.services.db

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

trait DbComponent {
  val dbConfig: DatabaseConfig[JdbcProfile]
  val db: JdbcProfile#Backend#Database = dbConfig.db
}

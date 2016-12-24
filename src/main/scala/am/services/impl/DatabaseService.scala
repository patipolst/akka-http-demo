package am.services.impl

import am.services.DatabaseServiceLike
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import slick.driver._

class DatabaseService(jdbcUrl: String, dbUser: String, dbPassword: String) extends DatabaseServiceLike {
  private val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(jdbcUrl)
  hikariConfig.setUsername(dbUser)
  hikariConfig.setPassword(dbPassword)

  private val dataSource = new HikariDataSource(hikariConfig)

  val driver = H2Driver
  import driver.api._

  val db = Database.forDataSource(dataSource)
  db.createSession()
}

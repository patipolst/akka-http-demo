package am.services.impl

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import slick.driver.H2Driver

class DatabaseService(jdbcUrl: String, dbUser: String, dbPassword: String) {
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

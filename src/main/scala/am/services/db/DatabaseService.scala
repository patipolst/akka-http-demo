package am.services.db

import com.zaxxer.hikari.{ HikariConfig, HikariDataSource }
import slick.driver._

class DatabaseService(jdbcUrl: String, dbUser: String, dbPassword: String) extends DbComponent {
  private val config = new HikariConfig()
  config.setJdbcUrl(jdbcUrl)
  config.setUsername(dbUser)
  config.setPassword(dbPassword)

  private val dataSource = new HikariDataSource(config)

  val driver = H2Driver
  import driver.api._

  val db = Database.forDataSource(dataSource)
  db.createSession()
}

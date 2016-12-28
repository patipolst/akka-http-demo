package am.utils

import com.typesafe.config.ConfigFactory
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

trait Config {
  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  private val jwtConfig = config.getConfig("jwt")

  val httpHost = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")
  val jwtKey = jwtConfig.getString("key")
  val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("database")
}

package am.services

import slick.driver.JdbcProfile

trait DatabaseServiceLike {
  val driver: JdbcProfile
  val db: driver.api.Database
}

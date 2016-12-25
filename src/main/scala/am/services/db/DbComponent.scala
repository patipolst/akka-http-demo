package am.services.db

import slick.driver.JdbcProfile

trait DbComponent {
  val driver: JdbcProfile
  val db: driver.api.Database
}

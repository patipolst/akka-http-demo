package am

import am.api.HttpService
import am.models._
import am.services._
import am.utils.Config
import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }
import org.scalatest.{ BeforeAndAfter, WordSpec, GivenWhenThen, Matchers }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.heikoseeberger.akkahttpcirce.CirceSupport
import pdi.jwt.{JwtCirce, JwtClaim, JwtAlgorithm}

trait BaseServiceSpec extends WordSpec with Matchers with GivenWhenThen with BeforeAndAfter
  with ScalatestRouteTest with CirceSupport with Config {

  private val addressesService = new AddressesService(dbConfig)
  private val usersService = new UsersService(dbConfig)
  private val httpService = new HttpService(usersService, addressesService)

  val baseAddressesUri = "/addresses"
  val baseUsersUri = "/users"

  private val jwtClaim = JwtClaim("""{"name": "test"}""")
  val jwtToken = JwtCirce.encode(jwtClaim, jwtKey, JwtAlgorithm.HS256)

  val addressesRoute = httpService.addressesRouter.route
  val usersRoute = httpService.usersRouter.route

  before {
    addressesService.initializeTable
    usersService.initializeTable
  }

  after {
    usersService.dropTable
    addressesService.dropTable
  }

  def createAddressesList(size: Int): Seq[Address] = {
    val savedAddresses = (1 to size).map { i =>
      Address(None, s"Street $i", s"City $i")
    }.map(addressesService.createAddress)
    Await.result(Future.sequence(savedAddresses), 10.seconds)
  }

  def createUsersList(size: Int): Seq[User] = {
    val savedUsers = (1 to size).map { i =>
      User(None, s"Name $i", i, i.toString)
    }.map(usersService.createUser)
    Await.result(Future.sequence(savedUsers), 10.seconds)
  }

}

package am.services

import am.BaseServiceSpec
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.StatusCodes._

class UsersServiceSpec extends BaseServiceSpec {

  trait DbContext {
    val testAddresses = createAddressesList(5)
    val testUsers = createUsersList(5)
  }

  "Users service" should {

    "retrieve users list" in new DbContext {
      Given("uri path")
      val uriPath = baseUsersUri

      When("send the request")
      Get(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
        usersRoute ~> check {
          Then("return all users successfully")
          status should be (OK)
      }
    }
  }
}

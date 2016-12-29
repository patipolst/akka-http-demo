package am.services

import am.BaseServiceSpec
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }

class AddressesServiceSpec extends BaseServiceSpec {

  trait FullAddresses {
    val testAddresses = createAddressesList(5)
    val testUsers = createUsersList(5)
  }

  trait EmptyAddresses {
  }

  "Addresses service" should {
    "retrieve addresses list" in new FullAddresses {
      Given("uri path")
      val uriPath = baseAddressesUri

      When("send the request")
      Get(uriPath) ~> addressesRoute ~> check {
        Then("return all addresses successfully")
        status should be (OK)
      }
    }

    "retrieve empty addresses list" in new EmptyAddresses {
      Given("uri path")
      val uriPath = baseAddressesUri

      When("send the request")
      Get(uriPath) ~> addressesRoute ~> check {
        Then("return addresses not found successfully")
        status should be (NotFound)
      }
    }

    "create new address" in new EmptyAddresses {
      Given("uri path and json")
      val uriPath = baseAddressesUri
      val json = """{
        "street": "Silom",
        "city": "Bangkok"
      }"""

      When("send the request")
      Post(uriPath).withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      addressesRoute ~> check {
        Then("return addresses not found successfully")
        status should be (Created)
      }
    }

    "retrieve address by id" in new FullAddresses {
      Given("uri path")
      val testAddress = testAddresses(0)
      val uriPath = s"${baseAddressesUri}/${testAddress.id.get}"

      When("send the request")
      Get(uriPath) ~> addressesRoute ~> check {
        Then("return address successfully")
        status should be (OK)
      }
    }

    "update address by id and retrieve it" in new FullAddresses {
      Given("uri path and json")
      val testAddress = testAddresses(0)
      val uriPath = s"${baseAddressesUri}/${testAddress.id.get}"
      val json = """{
        "street": "Sukhumvit"
      }"""

      When("send the request")
      Put(uriPath).withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      addressesRoute ~> check {
        Then("update address successfully")
        status should be (OK)
      }
    }

    "delete address" in new FullAddresses {
      Given("uri path")
      val testAddress = testAddresses(0)
      val uriPath = s"${baseAddressesUri}/${testAddress.id.get}"

      When("send the request")
      Delete(uriPath) ~> addressesRoute ~> check {
        Then("update address successfully")
        status should be (OK)
      }
    }

  }
}

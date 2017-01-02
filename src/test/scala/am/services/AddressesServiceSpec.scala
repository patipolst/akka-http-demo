package am.services

import am.BaseServiceSpec
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }

class AddressesServiceSpec extends BaseServiceSpec {
  "Addresses service" should {
    "retrieve addresses list" in new FullRows {
      Given("uri path")
      val uriPath = baseAddressesUri

      When("send the request")
      Get(uriPath) ~> routes ~> check {
        Then("return all addresses successfully")
        status should be (OK)
      }
    }

    "retrieve empty addresses list" in new EmptyRow {
      Given("uri path")
      val uriPath = baseAddressesUri

      When("send the request")
      Get(uriPath) ~> routes ~> check {
        Then("return addresses not found successfully")
        status should be (NotFound)
      }
    }

    "handle failure on retrieve addresses list" in new FailureCase {
      Given("uri path")
      val uriPath = baseAddressesUri

      When("send the request")
      Get(uriPath) ~> routes ~> check {
        Then("respond server error successfully")
        status should be (InternalServerError)
      }
    }

    "create new address" in new EmptyRow {
      Given("uri path and json")
      val uriPath = baseAddressesUri
      val json = """{
        "street": "Silom",
        "city": "Bangkok"
      }"""

      When("send the request")
      Post(uriPath).withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("create address successfully")
        status should be (Created)
      }
    }

    "create new address with empty string in json" in new EmptyRow {
      Given("uri path and json")
      val uriPath = baseAddressesUri
      val json = """{
        "street": "Silom",
        "city": ""
      }"""

      When("send the request")
      Post(uriPath).withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("create address unsuccessfully")
        status should be (BadRequest)
      }
    }

    "create new address with malformed json" in new EmptyRow {
      Given("uri path and json")
      val uriPath = baseAddressesUri
      val json = """{
        "street": "Silom",
        "city": 1
      }"""

      When("send the request")
      Post(uriPath).withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("create address unsuccessfully")
        status should be (BadRequest)
      }
    }

    "handle failure on create new address" in new FailureCase {
      Given("uri path and json")
      val uriPath = baseAddressesUri
      val json = """{
        "street": "Silom",
        "city": "Bangkok"
      }"""

      When("send the request")
      Post(uriPath).withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("respond server error successfully")
        status should be (InternalServerError)
      }
    }

    "retrieve address by id" in new FullRows {
      Given("uri path")
      val testAddress = testAddresses(0)
      val uriPath = s"${baseAddressesUri}/${testAddress.id.get}"

      When("send the request")
      Get(uriPath) ~> routes ~> check {
        Then("return address successfully")
        status should be (OK)
      }
    }

    "retrieve none existing address by id" in new EmptyRow {
      Given("uri path")
      val testAddressId = 10
      val uriPath = s"${baseAddressesUri}/${testAddressId}"

      When("send the request")
      Get(uriPath) ~> routes ~> check {
        Then("return address unsuccessfully")
        status should be (NotFound)
      }
    }


    "handle failure on retrieve address by id" in new FailureCase {
      Given("uri path")
      val testAddressId = 10
      val uriPath = s"${baseAddressesUri}/${testAddressId}"

      When("send the request")
      Get(uriPath) ~> routes ~> check {
        Then("respond server error successfully")
        status should be (InternalServerError)
      }
    }

    "update address by id and retrieve it" in new FullRows {
      Given("uri path and json")
      val testAddress = testAddresses(0)
      val uriPath = s"${baseAddressesUri}/${testAddress.id.get}"
      val json = """{
      }"""

      When("send the request")
      Put(uriPath).withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("update address successfully")
        status should be (OK)
      }
    }

    "update none existing address by id and retrieve it" in new EmptyRow {
      Given("uri path and json")
      val testAddressId = 10
      val uriPath = s"${baseAddressesUri}/${testAddressId}"
      val json = """{
        "city": "Pattaya"
      }"""

      When("send the request")
      Put(uriPath).withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("update address unsuccessfully")
        status should be (BadRequest)
      }
    }

    "update address by id with malformed json and retrieve it" in new EmptyRow {
      Given("uri path and json")
      val testAddressId = 10
      val uriPath = s"${baseAddressesUri}/${testAddressId}"
      val json = """{
        "street": 1
      }"""

      When("send the request")
      Put(uriPath).withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("update address unsuccessfully")
        status should be (BadRequest)
      }
    }

    "handle failure on update address by id and retrieve it" in new FailureCase {
      Given("uri path and json")
      val testAddressId = 10
      val uriPath = s"${baseAddressesUri}/${testAddressId}"
      val json = """{
      }"""

      When("send the request")
      Put(uriPath).withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("respond server error successfully")
        status should be (InternalServerError)
      }
    }

    "delete address" in new FullRows {
      Given("uri path")
      val testAddress = testAddresses(0)
      val uriPath = s"${baseAddressesUri}/${testAddress.id.get}"

      When("send the request")
      Delete(uriPath) ~> routes ~> check {
        Then("delete address successfully")
        status should be (OK)
      }
    }

    "delete none exsiting address" in new EmptyRow {
      Given("uri path")
      val testAddressId = 10
      val uriPath = s"${baseAddressesUri}/${testAddressId}"

      When("send the request")
      Delete(uriPath) ~> routes ~> check {
        Then("delete address unsuccessfully")
        status should be (BadRequest)
      }
    }

    "handle failure on delete address" in new FailureCase {
      Given("uri path")
      val testAddressId = 10
      val uriPath = s"${baseAddressesUri}/${testAddressId}"

      When("send the request")
      Delete(uriPath) ~> routes ~> check {
        Then("respond server error successfully")
        status should be (InternalServerError)
      }
    }

  }
}

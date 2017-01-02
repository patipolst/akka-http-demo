package am.services

import am.BaseServiceSpec
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }

class UsersServiceSpec extends BaseServiceSpec {
  "Users service" should {
    "retrieve useres list" in new FullRows {
      Given("uri path")
      val uriPath = baseUsersUri

      When("send the request")
      Get(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("return all useres successfully")
        status should be (OK)
      }
    }

    "retrieve empty useres list" in new EmptyRow {
      Given("uri path")
      val uriPath = baseUsersUri

      When("send the request")
      Get(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("return useres not found successfully")
        status should be (NotFound)
      }
    }

    "handle failure on retrieve useres list" in new FailureCase {
      Given("uri path")
      val uriPath = baseUsersUri

      When("send the request")
      Get(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("respond server error successfully")
        status should be (InternalServerError)
      }
    }

    "retrieve useres with addresses list" in new FullRows {
      Given("uri path")
      val uriPath = s"${baseUsersUri}?address=true"

      When("send the request")
      Get(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("return all useres successfully")
        status should be (OK)
      }
    }

    "retrieve empty useres with addresses list" in new EmptyRow {
      Given("uri path")
      val uriPath = s"${baseUsersUri}?address=true"

      When("send the request")
      Get(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("return useres not found successfully")
        status should be (NotFound)
      }
    }

    "handle failure on retrieve useres with addresses list" in new FailureCase {
      Given("uri path")
      val uriPath = s"${baseUsersUri}?address=true"

      When("send the request")
      Get(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("respond server error successfully")
        status should be (InternalServerError)
      }
    }

    "create new user" in new FullRows {
      Given("uri path and json")
      val uriPath = baseUsersUri
      val json = """{
        "name": "Test",
        "age": 19,
        "addressId": "2"
      }"""

      When("send the request")
      Post(uriPath).withHeaders(RawHeader("Authorization", jwtToken))
      .withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("create user successfully")
        status should be (Created)
      }
    }

    "create new user with empty string in json" in new EmptyRow {
      Given("uri path and json")
      val uriPath = baseUsersUri
      val json = """{
        "name": "",
        "age": 20,
        "addressId": "2"
      }"""

      When("send the request")
      Post(uriPath).withHeaders(RawHeader("Authorization", jwtToken))
      .withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("create user unsuccessfully")
        status should be (BadRequest)
      }
    }

    "create new user with malformed json" in new EmptyRow {
      Given("uri path and json")
      val uriPath = baseUsersUri
      val json = """{
        "name": "GG",
        "age": "aa",
        "addressId": "2"
      }"""

      When("send the request")
      Post(uriPath).withHeaders(RawHeader("Authorization", jwtToken))
      .withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("create user unsuccessfully")
        status should be (BadRequest)
      }
    }

    "handle failure on create new user" in new FailureCase {
      Given("uri path and json")
      val uriPath = baseUsersUri
      val json = """{
        "name": "Test",
        "age": 19,
        "addressId": "2"
      }"""

      When("send the request")
      Post(uriPath).withHeaders(RawHeader("Authorization", jwtToken))
      .withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("respond server error successfully")
        status should be (InternalServerError)
      }
    }

    "retrieve user by id" in new FullRows {
      Given("uri path")
      val testUser = testUsers(0)
      val uriPath = s"${baseUsersUri}/${testUser.id.get}"

      When("send the request")
      Get(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("return user successfully")
        status should be (OK)
      }
    }

    "retrieve none existing user by id" in new EmptyRow {
      Given("uri path")
      val testUserId = 10
      val uriPath = s"${baseUsersUri}/${testUserId}"

      When("send the request")
      Get(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("return user unsuccessfully")
        status should be (NotFound)
      }
    }

    "handle failure on retrieve user by id" in new FailureCase {
      Given("uri path")
      val testUserId = 10
      val uriPath = s"${baseUsersUri}/${testUserId}"

      When("send the request")
      Get(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("respond server error successfully")
        status should be (InternalServerError)
      }
    }

    "retrieve user with address by id" in new FullRows {
      Given("uri path")
      val testUser = testUsers(0)
      val uriPath = s"${baseUsersUri}/${testUser.id.get}?address=true"

      When("send the request")
      Get(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("return user successfully")
        status should be (OK)
      }
    }

    "retrieve none existing user with address by id" in new EmptyRow {
      Given("uri path")
      val testUserId = 10
      val uriPath = s"${baseUsersUri}/${testUserId}?address=true"

      When("send the request")
      Get(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("return user unsuccessfully")
        status should be (NotFound)
      }
    }

    "handle failure on retrieve user with address by id" in new FailureCase {
      Given("uri path")
      val testUserId = 10
      val uriPath = s"${baseUsersUri}/${testUserId}?address=true"

      When("send the request")
      Get(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("respond server error successfully")
        status should be (InternalServerError)
      }
    }

    "update user by id and retrieve it" in new FullRows {
      Given("uri path and json")
      val testUser = testUsers(0)
      val uriPath = s"${baseUsersUri}/${testUser.id.get}"
      val json = """{
      }"""

      When("send the request")
      Put(uriPath).withHeaders(RawHeader("Authorization", jwtToken))
      .withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("update user successfully")
        status should be (OK)
      }
    }

    "update none existing user by id and retrieve it" in new EmptyRow {
      Given("uri path and json")
      val testUserId = 10
      val uriPath = s"${baseUsersUri}/${testUserId}"
      val json = """{
        "name": "Harry"
      }"""

      When("send the request")
      Put(uriPath).withHeaders(RawHeader("Authorization", jwtToken))
      .withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("update user unsuccessfully")
        status should be (BadRequest)
      }
    }

    "update user by id with malformed json and retrieve it" in new EmptyRow {
      Given("uri path and json")
      val testUserId = 10
      val uriPath = s"${baseUsersUri}/${testUserId}"
      val json = """{
        "age": "sss"
      }"""

      When("send the request")
      Put(uriPath).withHeaders(RawHeader("Authorization", jwtToken))
      .withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("update user unsuccessfully")
        status should be (BadRequest)
      }
    }

    "handle failure on update user by id and retrieve it" in new FailureCase {
      Given("uri path and json")
      val testUserId = 10
      val uriPath = s"${baseUsersUri}/${testUserId}"
      val json = """{
      }"""

      When("send the request")
      Put(uriPath).withHeaders(RawHeader("Authorization", jwtToken))
      .withEntity(HttpEntity(ContentTypes.`application/json`, json)) ~>
      routes ~> check {
        Then("respond server error successfully")
        status should be (InternalServerError)
      }
    }

    "delete user" in new FullRows {
      Given("uri path")
      val testUser = testUsers(0)
      val uriPath = s"${baseUsersUri}/${testUser.id.get}"

      When("send the request")
      Delete(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("delete user successfully")
        status should be (OK)
      }
    }

    "delete none exsiting user" in new EmptyRow {
      Given("uri path")
      val testUserId = 10
      val uriPath = s"${baseUsersUri}/${testUserId}"

      When("send the request")
      Delete(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("delete user unsuccessfully")
        status should be (BadRequest)
      }
    }

    "handle failure on delete user" in new FailureCase {
      Given("uri path")
      val testUserId = 10
      val uriPath = s"${baseUsersUri}/${testUserId}"

      When("send the request")
      Delete(uriPath).withHeaders(RawHeader("Authorization", jwtToken)) ~>
      routes ~> check {
        Then("respond server error successfully")
        status should be (InternalServerError)
      }
    }

  }
}

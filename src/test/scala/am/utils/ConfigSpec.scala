package am.utils

import org.scalatest.{FeatureSpecLike, GivenWhenThen, Matchers}

class ConfigSpec extends Config with FeatureSpecLike with Matchers with GivenWhenThen {

	feature("Load httpConfig correctly") {
		scenario("Load httpHost correctly") {
			Given("the correct host")
			val expectedHost = "0.0.0.0"

			When("load config from file")
			val result = httpHost

			Then("return expected host correctly")
			result should be (expectedHost)
		}

		scenario("Load httpPort correctly") {
			Given("the correct host")
			val expectedPort = 9000

			When("load config from file")
			val result = httpPort

			Then("return expected port correctly")
			result should be (expectedPort)
		}
	}

	feature("Load jwtConfig correctly") {
		scenario("Load jwtHost correctly") {
			Given("the correct key")
			val expectedKey = "secret"

			When("load config from file")
			val result = jwtKey

			Then("return expected host correctly")
			result should be (expectedKey)
		}
	}
}

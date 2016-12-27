package am.utils

import am.models._
import com.wix.accord.dsl._

object Validator {
  implicit val addressValidator = validator[Address] { address =>
    address.street is notEmpty
    address.city is notEmpty
  }

  implicit val userValidator = validator[User] { user =>
    user.name is notEmpty
    user.age is > (18)
  }
}

package am.utils

import am.models._
import com.wix.accord.dsl._
import com.wix.accord._, ViolationBuilder._

trait ValidationMethods {
  def oneOf[ T <: AnyRef ]( options : T* ): Validator[ T ] =
  new NullSafeValidator[ T ](
    test    = options.contains,
    failure = _ -> s"is not one of (${ options.mkString( "," ) })"
  )
}

object Validator extends ValidationMethods {
  implicit val addressValidator = validator[Address] { address =>
    address.street is notEmpty
    address.city is notEmpty
  }

  implicit val userValidator = validator[User] { user =>
    user.name is notEmpty
    user.age is > (18)
  }
}

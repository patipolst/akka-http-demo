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

  def letter[ T <: String ]: Validator[ T ] =
  new NullSafeValidator[ T ](
    test    = _.forall(_.isLetter),
    failure = _ -> s"must be letter"
  )

  def digit[ T <: String ]: Validator[ T ] =
  new NullSafeValidator[ T ](
    test    = _.forall(_.isDigit),
    failure = _ -> s"must be digit"
  )
}

object Validator extends ValidationMethods {
  implicit val addressValidator = validator[Address] { address =>
    address.street is notEmpty
    address.street is letter
    address.city is notEmpty
    address.city is letter
  }

  implicit val addressUpdateValidator = validator[AddressUpdate] { addressUpdate =>
    addressUpdate.street.each is notEmpty
    addressUpdate.street.each is letter
    addressUpdate.city.each is notEmpty
    addressUpdate.city.each is letter
  }

  implicit val userValidator = validator[User] { user =>
    user.name is notEmpty
    user.name is letter
    user.age is > (0)
  }

  implicit val userUpdateValidator = validator[UserUpdate] { userUpdate =>
    userUpdate.name.each is notEmpty
    userUpdate.name.each is letter
    userUpdate.age.each is > (0)
  }
}

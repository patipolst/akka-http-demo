package am.models

case class User(
  id: Option[String] = None,
  name: String,
  team: String
)

case class UserUpdate(name: Option[String] = None, team: Option[String] = None) {
  def merge(user: User): User =
    user.copy(name = name.getOrElse(user.name), team = team.getOrElse(user.team))
}

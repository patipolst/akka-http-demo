package am.models.formats

case class ErrorResponseFormat(
  errors: Seq[String],
  code: Int,
  status: String
)

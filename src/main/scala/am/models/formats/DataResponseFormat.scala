package am.models.formats

import io.circe.Json

case class DataResponseFormat(
  data: Json,
  code: Int,
  status: String
)

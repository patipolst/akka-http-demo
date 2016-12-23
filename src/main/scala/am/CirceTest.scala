package am

import am.utils._
import io.circe._
import io.circe.parser._
import io.circe.generic.auto._
// import io.circe.generic.JsonCodec
// import io.circe.generic.semiauto._
import io.circe.syntax._

case class Person(id: String, name: String, books: List[String])

object CirceTest extends App with Helper {
  val rawJson: String = """
  {
    "id": "  123  ",
    "name": "  bar  ",
    "books": ["a", "b"]
  }"""

  val json = parse(rawJson).getOrElse(Json.Null)

  println(Console.CYAN + Person("1", "boom", List("a", "b")).asJson + Console.RESET)
  println(Console.MAGENTA + json + Console.RESET)


  val fields = json.hcursor.fields.getOrElse(Nil).toList
  println(Console.GREEN + fields + Console.RESET)

  val a = json.hcursor.downField("name").withFocus(_.mapString(_.trim))

  println(Console.CYAN + a.as[String] + Console.RESET)

  println(Console.GREEN + checkFields(json, List("id", "name", "books")) + Console.RESET)

  println(Console.CYAN + normalize(json) + Console.RESET)

  // println(Console.CYAN + json.hcursor.downField("id").as[Int] + Console.RESET)
  // println(Console.CYAN + json.hcursor.downField("name").get[String] + Console.RESET)

  System.exit(0)
}

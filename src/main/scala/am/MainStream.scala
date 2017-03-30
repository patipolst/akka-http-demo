package am

import akka.stream._
import akka.stream.scaladsl._
import akka.{ NotUsed, Done }
import akka.actor.ActorSystem
import akka.util.ByteString
import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths

object MainStream extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executor = system.dispatcher

  val source: Source[Int, NotUsed] = Source(1 to 100)
  val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

  def ex1() = {
    source.runForeach(i => println(i))(materializer)

    val result: Future[IOResult] =
      factorials
      .map(num => ByteString(s"$num\n"))
      .runWith(FileIO.toPath(Paths.get("factorials.txt")))

    def lineSink(filename: String): Sink[String, Future[IOResult]] =
      Flow[String]
      .map(s => ByteString(s + "\n"))
      .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)
      factorials.map(_.toString).runWith(lineSink("factorial2.txt"))
  }

  def ex2() = {
  val done: Future[Done] =
    factorials
    .zipWith(Source(0 to 100))((num, idx) => s"$idx! = $num")
    .throttle(1, 1.second, 1, ThrottleMode.shaping)
    .runForeach(println)
  }

  ex1()
  ex2()

  val sourceEmittingTheSameElement = Source.repeat("again and again").throttle(2, 1.second, 1, ThrottleMode.shaping).runForeach(println)
}
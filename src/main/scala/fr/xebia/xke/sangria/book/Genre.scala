package fr.xebia.xke.sangria.book

import akka.http.scaladsl.unmarshalling.Unmarshaller
import io.circe.Decoder.Result
import io.circe.Json.JString
import io.circe.{Decoder, DecodingFailure, Encoder, HCursor, Json}

sealed trait Genre

object Genre {

  case object Comedy extends Genre

  case object SyFy extends Genre

  private def parser(s: String): Option[Genre] = s match {
    case "comedy" => Some(Comedy)
    case "syfy" => Some(SyFy)
    case _ => None
  }

  implicit val encoder = new Encoder[Genre] {
    override def apply(g: Genre): Json = g match {
      case Comedy => JString("comedy")
      case SyFy => JString("syfy")
    }
  }

  implicit val decoder = new Decoder[Genre] {
    override def apply(c: HCursor): Result[Genre] = c.as[String].flatMap(parser(_).toRight(DecodingFailure("Not a valid genre", List.empty)))
  }

  // Akka http unmarshaller for query params
  implicit val genreUnmarshaller = Unmarshaller.strict(parser).map(_.getOrElse(throw new IllegalArgumentException("Not a valid genre")))
}

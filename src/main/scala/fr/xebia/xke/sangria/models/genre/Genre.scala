package fr.xebia.xke.sangria.models.genre

import akka.http.scaladsl.unmarshalling.Unmarshaller
import io.circe.Decoder.Result
import io.circe._
import sangria.macros.derive.deriveEnumType

sealed trait Genre

object Genre {

  case object Comedy extends Genre
  case object Roman extends Genre
  case object SciFi extends Genre
  case object Historical extends Genre

  private def parser(s: String): Option[Genre] = s match {
    case "Comedy" => Some(Comedy)
    case "Roman" => Some(Roman)
    case "SciFi" => Some(SciFi)
    case "Historical" => Some(Historical)
    case _ => None
  }

  implicit val encoder = new Encoder[Genre] {
    override def apply(g: Genre): Json = Json.fromString(g.toString)
  }

  implicit val decoder = new Decoder[Genre] {
    override def apply(c: HCursor): Result[Genre] = c.as[String].flatMap(parser(_).toRight(DecodingFailure("Not a valid genre", List.empty)))
  }

  // Akka http unmarshaller for query params
  implicit val genreUnmarshaller = Unmarshaller.strict(parser).map(_.getOrElse(throw new IllegalArgumentException("Not a valid genre")))
}

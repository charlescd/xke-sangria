package fr.xebia.xke.sangria.models.user

import io.circe.Decoder.Result
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder, HCursor, Json}

sealed trait Access

object Access {

  case object Author extends Access
  case object No extends Access

  private def parser(s: String): Access = s match {
    case "Author" => Author
    case _ => No
  }

  implicit val encoder = new Encoder[Access] {
    override def apply(g: Access): Json = Json.fromString(g.toString)
  }

  implicit val decoder = new Decoder[Access] {
    override def apply(c: HCursor): Result[Access] = c.as[String].map(parser)
  }
}

case class User(login: String, access: Access)

object User {

  //REST
  implicit val decoder = deriveDecoder[User]
  implicit val encoder = deriveEncoder[User]
}

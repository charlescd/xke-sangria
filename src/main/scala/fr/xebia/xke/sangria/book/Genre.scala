package fr.xebia.xke.sangria.book

import io.circe.Json.JString
import io.circe.{Decoder, Encoder, Json}

sealed trait Genre

object Genre {

  case object Comedy extends Genre

  implicit val encoder = new Encoder[Genre] {
    final def apply(a: Genre): Json = a match {
      case Comedy => JString("comedy")
    }
  }

}

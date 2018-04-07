package fr.xebia.xke.sangria.jwt

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class User(login: String)

object User {
  implicit val decoder = deriveDecoder[User]
  implicit val encoder = deriveEncoder[User]
}

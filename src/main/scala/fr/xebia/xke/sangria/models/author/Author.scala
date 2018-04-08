package fr.xebia.xke.sangria.models.author

import java.util.UUID

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import sangria.macros.derive.{deriveInputObjectType, deriveObjectType}
import fr.xebia.xke.sangria.graphql.Scalar._

case class Author(id: UUID, lastName: String, firstName: String)

object Author {
  // REST
  implicit val decoder = deriveDecoder[Author]
  implicit val encoder = deriveEncoder[Author]
}

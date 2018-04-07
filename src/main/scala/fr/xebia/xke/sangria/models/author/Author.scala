package fr.xebia.xke.sangria.models.author

import java.util.UUID

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import sangria.macros.derive.deriveObjectType
import fr.xebia.xke.sangria.graphql.Scalar._

case class Author(id: UUID, lastName: String, firstName: String)

object Author {
  //GraphQL
  implicit val AuthorType = deriveObjectType[Unit, Author]()

  // REST
  implicit val decoder = deriveDecoder[Author]
  implicit val encoder = deriveEncoder[Author]
}

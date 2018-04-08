package fr.xebia.xke.sangria.graphql

import fr.xebia.xke.sangria.graphql.Scalar.UuidType
import fr.xebia.xke.sangria.models.book.BookService
import fr.xebia.xke.sangria.models.user.Access
//import sangria.marshalling.FromInput
import sangria.marshalling.circe._
import sangria.schema._

object SchemaDefinition {

  val schema: Schema[BookService, Unit] = ???
}

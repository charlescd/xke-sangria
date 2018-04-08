package fr.xebia.xke.sangria.models.book

import java.time.OffsetDateTime
import java.util.UUID

import fr.xebia.xke.sangria.models.author.Author
import fr.xebia.xke.sangria.models.genre.Genre
import fr.xebia.xke.sangria.models.Formatters._
import fr.xebia.xke.sangria.graphql.Scalar._
import io.circe.generic.semiauto._
import sangria.macros.derive._
import sangria.schema.{InputObjectType, ObjectType}

case class Book(isbn: UUID,
                title: String,
                synopsis: String,
                date: OffsetDateTime,
                author: Author,
                genre: Genre)

case class BookInput(title: String,
                     synopsis: String,
                     date: OffsetDateTime,
                     authorId: UUID,
                     genre: Genre)

object Book {

  //GraphQL
  implicit val BookType: ObjectType[Unit, Book] = deriveObjectType[Unit, Book]()

  // REST
  implicit val decoder = deriveDecoder[Book]
  implicit val encoder = deriveEncoder[Book]
}

object BookInput {

  //GraphQL
  implicit val BookInputType:InputObjectType[BookInput] =
    deriveInputObjectType[BookInput]()

  implicit val decoder = deriveDecoder[BookInput]
  implicit val encoder = deriveEncoder[BookInput]
}

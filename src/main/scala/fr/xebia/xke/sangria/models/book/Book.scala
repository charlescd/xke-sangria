package fr.xebia.xke.sangria.models.book

import java.time.OffsetDateTime
import java.util.UUID

import fr.xebia.xke.sangria.graphql.Scalar._
import fr.xebia.xke.sangria.models.author.Author
import fr.xebia.xke.sangria.models.genre.Genre
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.semiauto._
import sangria.macros.derive._

case class Book(isbn: UUID,
                title: String,
                synopsis: String,
                date: OffsetDateTime,
                author: Author,
                genre: Genre)

object Book {
  implicit val uuidEncoder = new Encoder[UUID] {
    override def apply(uuid: UUID): Json = Json.fromString(uuid.toString)
  }

  implicit val uuidDecoder = new Decoder[UUID] {
    override def apply(c: HCursor): Result[UUID] = c.as[String].map(UUID.fromString)
  }

  implicit val offsetDateTimeEncoder = new Encoder[OffsetDateTime] {
    override def apply(date: OffsetDateTime): Json = Json.fromString(date.toString)
  }

  implicit val offsetDateTimeDecoder = new Decoder[OffsetDateTime] {
    override def apply(c: HCursor): Result[OffsetDateTime] = c.as[String].map(OffsetDateTime.parse)
  }

  //GraphQL
  implicit val BookType = deriveObjectType[Unit, Book]()

  // REST
  implicit val decoder = deriveDecoder[Book]
  implicit val encoder = deriveEncoder[Book]
}

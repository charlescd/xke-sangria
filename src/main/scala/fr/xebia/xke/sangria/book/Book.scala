package fr.xebia.xke.sangria.book

import java.time.OffsetDateTime
import java.util.UUID

import io.circe.generic.semiauto._
import sangria.macros.derive._

case class Book(isbn: UUID,
                title: String,
                synopsis: String,
                date: OffsetDateTime,
                author: Author,
                genre: Genre)

object Book {
  //GraphQL
  implicit val BookType = deriveObjectType[Unit, Book]()

  // REST
  implicit val decoder = deriveDecoder[Book]
  implicit val encoder = deriveEncoder[Book]
}

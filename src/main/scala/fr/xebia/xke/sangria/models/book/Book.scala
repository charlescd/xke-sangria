package fr.xebia.xke.sangria.models.book

import java.time.OffsetDateTime
import java.util.UUID

import fr.xebia.xke.sangria.models.author.Author
import fr.xebia.xke.sangria.models.genre.Genre
import fr.xebia.xke.sangria.models.Formatters._
import io.circe.generic.semiauto._

case class Book(isbn: UUID,
                title: String,
                synopsis: String,
                date: OffsetDateTime,
                author: Author,
                genre: Genre)

object Book {
  implicit val decoder = deriveDecoder[Book]
  implicit val encoder = deriveEncoder[Book]
}

package fr.xebia.xke.sangria.book

import io.circe.generic.semiauto._
import sangria.macros.derive._

case class Book(id: Int, name: String)

object Book {
  implicit val BookType = deriveObjectType[Unit, Book]()

  implicit val decoder = deriveDecoder[Book]
  implicit val encoder = deriveEncoder[Book]
}

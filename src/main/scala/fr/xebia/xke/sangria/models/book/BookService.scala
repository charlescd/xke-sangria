package fr.xebia.xke.sangria.models.book

import java.time.OffsetDateTime
import java.util.UUID

import fr.xebia.xke.sangria.models.genre.Genre

class BookService(bookRepository: BookRepository) {
  def search(from: Option[OffsetDateTime], to: Option[OffsetDateTime], author: Option[String], genre: Option[Genre]): Seq[Book] = {
    bookRepository.books
      .filter(book => from.isEmpty || from.get.isBefore(book.date))
      .filter(book => to.isEmpty || to.get.isAfter(book.date))
      .filter(book => author.isEmpty || author.get == book.author.lastName)
      .filter(book => genre.isEmpty || genre.get == book.genre)
  }


  def findBook(id: UUID): Option[Book] = {
    bookRepository.book(id)
  }

}

package fr.xebia.xke.sangria.models.book

import java.util.UUID

import fr.xebia.xke.sangria.models.filter.Filter

class BookService(bookRepository: BookRepository) {
  def search(filter: Option[Filter]): Seq[Book] = {
    filter.map { case Filter(from, to, author, genre) =>
      bookRepository.books
        .filter(book => from.isEmpty || from.get.isBefore(book.date))
        .filter(book => to.isEmpty || to.get.isAfter(book.date))
        .filter(book => author.isEmpty || author.get == book.author.lastName)
        .filter(book => genre.isEmpty || genre.get == book.genre)
    }.getOrElse(bookRepository.books)
  }


  def findBook(id: UUID): Option[Book] = {
    bookRepository.book(id)
  }

}
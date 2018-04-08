package fr.xebia.xke.sangria.models.book

import java.util.UUID

import fr.xebia.xke.sangria.graphql.SecureContext.MissingAuthor
import fr.xebia.xke.sangria.models.author.AuthorRepository
import fr.xebia.xke.sangria.models.filter.Filter

class BookService(bookRepository: BookRepository, authorRepository: AuthorRepository) {

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

  def createBook(bookInput: BookInput): Book =
    authorRepository.author(bookInput.authorId) match {
      case Some(author) =>
        val newBook = new Book(UUID.randomUUID(), bookInput.title, bookInput.synopsis, bookInput.date, author, bookInput.genre)
        bookRepository.createBook(newBook)
      case _ => throw MissingAuthor(bookInput.authorId)
    }

}

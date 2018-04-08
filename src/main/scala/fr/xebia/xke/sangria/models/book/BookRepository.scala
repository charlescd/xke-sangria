package fr.xebia.xke.sangria.models.book

import java.time.OffsetDateTime
import java.util.UUID

import fr.xebia.xke.sangria.models.author.AuthorRepository.{Author1, Author2}
import fr.xebia.xke.sangria.models.book.BookRepository._
import fr.xebia.xke.sangria.models.genre.Genre.{Historical, Roman}

import scala.collection.mutable

class BookRepository {

  private val repo: mutable.MutableList[Book] = mutable.MutableList(Book1, Book2)

  def book(isbn: UUID) = repo.find(_.isbn == isbn)

  def books = repo

  def createBook(book: Book): Book = {
    repo += book
    book
  }
}

object BookRepository {
  val Book1 = Book(UUID.randomUUID(), "Les 3 mousquetaires", "c'est l'histoire de Dartagnan",
    OffsetDateTime.parse("1994-01-01T01:00:00Z"), Author1, Historical)

  val Book2 = Book(UUID.randomUUID(), "20000 lieues sous les mers", "c'est l'histoire de blabla",
    OffsetDateTime.now(), Author2, Roman)
}

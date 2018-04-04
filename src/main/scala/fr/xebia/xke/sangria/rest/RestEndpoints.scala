package fr.xebia.xke.sangria.rest

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import fr.xebia.xke.sangria.book.BookRepository

class RestEndpoints(bookRepository: BookRepository) {

  val fetchRoute =
    path("books" / IntNumber) { bookId =>
      get {
        complete(bookRepository.book(bookId))
      }
    }

  val fetchAllRoute =
    path("books") {
      get {
        complete(bookRepository.books)
      }
    }
}

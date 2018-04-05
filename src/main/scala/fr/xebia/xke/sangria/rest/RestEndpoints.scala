package fr.xebia.xke.sangria.rest

import java.time.OffsetDateTime

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshaller
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import fr.xebia.xke.sangria.book.{BookService, Genre}

class RestEndpoints(bookService: BookService) {

  implicit val uuidUnmarshaller: Unmarshaller[String, OffsetDateTime] = Unmarshaller.strict(OffsetDateTime.parse)

  val fetchRoute =
    path("books" / JavaUUID) { bookId =>
      get {
        complete(bookService.findBook(bookId))
      }
    }

  val searchRoute =
    path("books") {
      get {
        parameters("from".as[OffsetDateTime].?, "to".as[OffsetDateTime].?, "author".?, "genre".as[Genre].?) { (from, to, author, genre) =>
          complete(bookService.search(from, to, author, genre))
        }
      }
    }
}

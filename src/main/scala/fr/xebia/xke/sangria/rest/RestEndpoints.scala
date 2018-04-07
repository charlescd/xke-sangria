package fr.xebia.xke.sangria.rest

import java.time.OffsetDateTime

import akka.http.scaladsl.marshalling.Marshaller
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshaller
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import fr.xebia.xke.sangria.models.genre.Genre
import fr.xebia.xke.sangria.models.book.{Book, BookService}
import fr.xebia.xke.sangria.jwt.JwtSupport.Directives._
import fr.xebia.xke.sangria.jwt.User

class RestEndpoints(bookService: BookService) {

  implicit val dateUnmarshaller: Unmarshaller[String, OffsetDateTime] = Unmarshaller.strict(OffsetDateTime.parse)
  implicit val bookMarshaller: Marshaller[Book, HttpResponse] = ???
  implicit val booksMarshaller: Marshaller[Seq[Book], HttpResponse] = ???

  // Route simple
  val fetchRoute =
    path("books" / JavaUUID) { bookId =>
      get {
        complete(bookService.findBook(bookId))
      }
    }

  // Route complexe à gérer en REST, faire une recherche avec des query params.
  val searchRoute =
    path("books") {
      get {
        parameters("from".as[OffsetDateTime].?, "to".as[OffsetDateTime].?, "author".?, "genre".as[Genre].?) { (from, to, author, genre) =>
          complete(bookService.search(from, to, author, genre))
        }
      }
    }

  // Route qui nécessite d'être authentifié (token jwt dans le header : Authorization: Bearer XXXXX)
  val authenticatedRoute =
    path("users") {
      get {
        authenticate { user =>
          complete(???)
        }
      }
    }

  // Route pour s'authentifier (uniquement avec login = pg et password = 1234... c'est pour l'exemple ^^)
  val authenticateRoute =
    path("users" / "session") {
      get {
        parameters("login" ! "pg", "password" ! "1234") {
          generateToken(User("pg")) {
            complete("Ok")
          }
        }
      }
    }
}

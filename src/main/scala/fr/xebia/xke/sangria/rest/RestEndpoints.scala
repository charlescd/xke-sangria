package fr.xebia.xke.sangria.rest

import java.time.OffsetDateTime

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshaller
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import fr.xebia.xke.sangria.jwt.JwtSupport.Directives._
import fr.xebia.xke.sangria.jwt.User
import fr.xebia.xke.sangria.models.book.BookService
import fr.xebia.xke.sangria.models.filter.Filter

class RestEndpoints(bookService: BookService) {

  implicit val dateUnmarshaller: Unmarshaller[String, OffsetDateTime] = Unmarshaller.strict(OffsetDateTime.parse)

  // Route simple
  val fetchRoute =
    path("books" / JavaUUID) { bookId =>
      get {
        complete(bookService.findBook(bookId))
      }
    }

  // Route complexe à gérer, faire une recherche avec des query params.
  val searchRoute =
    path("books") {
      get {
        parameters("filter".as[Filter].?) { filter =>
          complete(bookService.search(filter))
        }
      }
    }

  // Route qui nécessite d'être authentifié, par exemple en tant qu'auteur (token jwt dans le header : Authorization: Bearer XXXXX)
  val authenticatedRoute =
    path("me" / "books") {
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

package fr.xebia.xke.sangria.rest

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import fr.xebia.xke.sangria.jwt.JwtSupport.Directives._
import fr.xebia.xke.sangria.models.author.AuthorRepository
import fr.xebia.xke.sangria.models.book.BookService
import fr.xebia.xke.sangria.models.filter.Filter
import fr.xebia.xke.sangria.models.user.{Access, UserRepository}

class RestEndpoints(bookService: BookService, authorRepository: AuthorRepository, userRepository: UserRepository) {

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
    path("me" / "authors") {
      get {
        authenticate { user =>
          user.access match {
            case Access.Author => complete(authorRepository.authors)
            case Access.No => complete(StatusCodes.Forbidden)
          }
        }
      }
    }

  // Route pour s'authentifier
  val authenticateRoute =
    path("users" / "session") {
      get {
        parameters("login", "password") { (login, password) =>
          userRepository.authenticate(login, password) match {
            case Some(user) => generateToken(user) {
              complete("Ok")
            }
            case _ => complete(StatusCodes.Unauthorized)
          }
        }
      }
    }
}

package fr.xebia.xke.sangria

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import fr.xebia.xke.sangria.graphql.GraphQLEndpoints
import fr.xebia.xke.sangria.models.author.AuthorRepository
import fr.xebia.xke.sangria.models.book.{BookRepository, BookService}
import fr.xebia.xke.sangria.models.user.UserRepository
import fr.xebia.xke.sangria.rest.RestEndpoints

object SangriaServer extends App {

  implicit val system = ActorSystem("sangria-server")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val bookRepository = new BookRepository()
  val authorRepository = new AuthorRepository()
  val userRepository = new UserRepository()
  val bookService = new BookService(bookRepository, authorRepository)

  val restEndpoints = new RestEndpoints(bookService, authorRepository, userRepository)

  Http().bindAndHandle(
    restEndpoints.fetchRoute
      ~ restEndpoints.searchRoute
      ~ restEndpoints.authenticatedRoute
      ~ restEndpoints.authenticateRoute,
    "0.0.0.0",
    8080
  )

}

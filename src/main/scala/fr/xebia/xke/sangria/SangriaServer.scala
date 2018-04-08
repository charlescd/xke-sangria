package fr.xebia.xke.sangria

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import fr.xebia.xke.sangria.models.book.{BookRepository, BookService}
import fr.xebia.xke.sangria.graphql.GraphQLEndpoints
import fr.xebia.xke.sangria.rest.RestEndpoints

object SangriaServer extends App {

  implicit val system = ActorSystem("sangria-server")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val bookRepository = new BookRepository()
  val bookService = new BookService(bookRepository)

  val graphqlEndpoints = new GraphQLEndpoints(bookService)
  val restEndpoints = new RestEndpoints(bookService)

  Http().bindAndHandle(
    graphqlEndpoints.graphqlRoute
      ~ graphqlEndpoints.graphiqlRoute

      ~ restEndpoints.fetchRoute
      ~ restEndpoints.searchRoute
      ~ restEndpoints.authenticatedRoute
      ~ restEndpoints.authenticateRoute,
    "0.0.0.0",
    8080
  )

}

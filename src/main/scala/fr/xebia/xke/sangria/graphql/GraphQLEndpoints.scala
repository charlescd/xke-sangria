package fr.xebia.xke.sangria.graphql

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, getFromResource, path, post}
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import fr.xebia.xke.sangria.jwt.JwtSupport.Directives.authenticateForGraphQL
import fr.xebia.xke.sangria.models.author.AuthorRepository
import fr.xebia.xke.sangria.models.book.BookService
import fr.xebia.xke.sangria.models.user.{User, UserRepository}
import io.circe.optics.JsonPath.root
import io.circe.{Json, JsonObject}
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.marshalling.circe._
import sangria.parser.QueryParser

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class GraphQLEndpoints(bookService: BookService, authorRepository: AuthorRepository, userRepository: UserRepository)(implicit ec: ExecutionContext) {

  val graphqlRoute: Route = ???

  val graphiqlRoute = path("graphiql") {
    get {
      getFromResource("graphiql.html")
    }
  }

  private def graphQLEndpoint(json: Json, maybeUser: Option[User]) = {
    root.query.string.getOption(json) match {
      case Some(query) =>
        val operation = root.operationName.string.getOption(json)
        val vars = Json.fromJsonObject(root.variables.obj.getOption(json).getOrElse(JsonObject()))

        QueryParser.parse(query) match {
          case Success(queryAst) =>
            val res = Executor.execute(SchemaDefinition.schema, queryAst, bookService, variables = vars, operationName = operation)
              .map(StatusCodes.OK -> _)
              .recover {
                case error: QueryAnalysisError => StatusCodes.BadRequest -> error.resolveError
                case error: ErrorWithResolver => StatusCodes.InternalServerError -> error.resolveError
              }

            complete(res)

          case Failure(error) =>
            complete(StatusCodes.BadRequest, Json.obj("error" -> Json.fromString(error.getMessage)))
        }

      case None =>
        complete(StatusCodes.BadRequest, Json.obj("error" -> Json.fromString("pas de query !")))
    }
  }
}

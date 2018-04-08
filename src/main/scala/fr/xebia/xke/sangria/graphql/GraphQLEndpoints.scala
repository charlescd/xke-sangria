package fr.xebia.xke.sangria.graphql

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, getFromResource, path, post}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import fr.xebia.xke.sangria.models.book.BookService
import io.circe.optics.JsonPath.root
import io.circe.{Json, JsonObject}
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.marshalling.circe._
import sangria.parser.QueryParser

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class GraphQLEndpoints(bookService: BookService)(implicit ec: ExecutionContext) {

  val graphqlRoute =
    path("graphql") {
      post {
        entity(as[Json]) { requestJson ⇒
          graphQLEndpoint(requestJson)
        }
      }
    }

  val graphiqlRoute = path("graphiql") {
    get {
      getFromResource("graphiql.html")
    }
  }

  private def graphQLEndpoint(json: Json) = {
    root.query.string.getOption(json) match {
      case Some(query) =>
        val operation = root.operationName.string.getOption(json)
        val vars = Json.fromJsonObject(root.variables.obj.getOption(json).getOrElse(JsonObject()))

        QueryParser.parse(query) match {
          case Success(queryAst) =>
            val res = Executor.execute(Query.schema, queryAst, bookService, variables = vars, operationName = operation)
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

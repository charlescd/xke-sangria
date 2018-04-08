package fr.xebia.xke.sangria.graphql

import fr.xebia.xke.sangria.graphql.Scalar.UuidType
import fr.xebia.xke.sangria.models.book.Book.BookType
import fr.xebia.xke.sangria.models.book.BookService
import fr.xebia.xke.sangria.models.filter.Filter.FilterType
import sangria.schema._
import sangria.marshalling.circe._

object Query {

  private val Uuid = Argument("isbn", UuidType)
  private val Filter = Argument("filter", OptionInputType(FilterType))

  private val QueryType = ObjectType("Query", fields[BookService, Unit](
    Field("book", OptionType(BookType),
      description = None,
      arguments = Uuid :: Nil,
      resolve = c => c.ctx.findBook(c arg Uuid)),

    Field("books", ListType(BookType),
      description = None,
      arguments = Filter :: Nil,
      resolve = c => c.ctx.search(c arg Filter))
  ))

  val schema = Schema(query = QueryType)

}

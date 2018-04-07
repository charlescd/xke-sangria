package fr.xebia.xke.sangria.graphql

import fr.xebia.xke.sangria.graphql.Scalar.UuidType
import fr.xebia.xke.sangria.models.book.Book.BookType
import fr.xebia.xke.sangria.models.book.BookRepository
import sangria.schema._

object Query {

  private val Uuid = Argument("isbn", UuidType)

  private val QueryType = ObjectType("Query", fields[BookRepository, Unit](
    Field("book", OptionType(BookType),
      description = None,
      arguments = Uuid :: Nil,
      resolve = c => c.ctx.book(c arg Uuid)),

    Field("books", ListType(BookType),
      description = None,
      resolve = _.ctx.books)
  ))

  val schema = Schema(query = QueryType)

}

package fr.xebia.xke.sangria.graphql

import fr.xebia.xke.sangria.book.Book.BookType
import fr.xebia.xke.sangria.book.BookRepository
import sangria.schema._


object Query {

  private val Id = Argument("id", IntType)

  private val QueryType = ObjectType("Query", fields[BookRepository, Unit](
    Field("book", OptionType(BookType),
      description = None,
      arguments = Id :: Nil,
      resolve = c => c.ctx.book(c arg Id)),

    Field("books", ListType(BookType),
      description = None,
      resolve = _.ctx.books)
  ))

  val schema = Schema(QueryType)

}

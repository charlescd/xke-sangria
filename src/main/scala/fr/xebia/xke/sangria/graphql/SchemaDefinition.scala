package fr.xebia.xke.sangria.graphql

import fr.xebia.xke.sangria.graphql.Scalar.UuidType
import fr.xebia.xke.sangria.models.author.Author.AuthorType
import fr.xebia.xke.sangria.models.book.Book.BookType
import fr.xebia.xke.sangria.models.book.BookInput.BookInputType
import fr.xebia.xke.sangria.models.filter.Filter.FilterType
import fr.xebia.xke.sangria.models.user.Access
import sangria.marshalling.circe._
import sangria.schema._

object SchemaDefinition {


  private val Uuid = Argument("isbn", UuidType)
  private val Filter = Argument("filter", OptionInputType(FilterType))
  private val BookInputArg = Argument("book", BookInputType)

  private val QueryType = ObjectType("Query", fields[SecureContext, Unit](
    Field("book", OptionType(BookType),
      description = None,
      arguments = Uuid :: Nil,
      resolve = c => c.ctx.bookService.findBook(c arg Uuid)),

    Field("books", ListType(BookType),
      description = None,
      arguments = Filter :: Nil,
      resolve = c => c.ctx.bookService.search(c arg Filter)),

    Field("authors", ListType(AuthorType),
      description = None,
      arguments = Nil,
      resolve = c => c.ctx.authorized(Access.Author)(c.ctx.authorRepository.authors))
  ))

  private val MutationType = ObjectType("Mutation", fields[SecureContext, Unit](
    Field(
      name = "createBook",
      fieldType = BookType,
      description = Some("add a book"),
      arguments = BookInputArg :: Nil,
      resolve = c => c.ctx.bookService.createBook(c arg BookInputArg)
    )
  ))

  val schema = Schema(query = QueryType, Some(MutationType))
}

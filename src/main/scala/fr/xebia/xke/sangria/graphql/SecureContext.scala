package fr.xebia.xke.sangria.graphql

import java.util.UUID

import fr.xebia.xke.sangria.models.author.AuthorRepository
import fr.xebia.xke.sangria.models.book.BookService
import fr.xebia.xke.sangria.models.user.{Access, User, UserRepository}
import sangria.execution.{ExceptionHandler, HandledException}

class SecureContext(val bookService: BookService,
                    val authorRepository: AuthorRepository,
                    val userRepository: UserRepository,
                    maybeUser: Option[User]) {

  def authorized[T](access: Access)(fn: => T) =
    maybeUser.filter(_.access == access).map(_ => fn).getOrElse(throw SecureContext.Forbidden("Forbidden"))

}


object SecureContext {

  case class Forbidden(m: String) extends Exception(m)
  case class MissingAuthor(uuid: UUID) extends Exception()

  val errorHandler = ExceptionHandler {
    case (m, Forbidden(message)) ⇒ HandledException(message)
    case (m, MissingAuthor(uuid)) ⇒ HandledException(s"Missing author with uuid: $uuid")
  }

}

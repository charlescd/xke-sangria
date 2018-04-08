package fr.xebia.xke.sangria.graphql

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

  val errorHandler = ExceptionHandler {
    case (m, Forbidden(message)) ⇒ HandledException(message)
  }

}
package fr.xebia.xke.sangria.models.author

import java.util.UUID

import fr.xebia.xke.sangria.models.author.AuthorRepository._


class AuthorRepository {

  private val repo = Seq(Author1, Author2)

  def author(firstName: String, lastName: String) = repo.find(author => author.firstName == firstName &&
    author.lastName == lastName)

  def author(id: UUID): Option[Author] = repo.find(_.id == id)

  def authors = repo
}

object AuthorRepository {
  val Author1 = Author(UUID.randomUUID(), "Dumas", "Alexandre")
  val Author2 = Author(UUID.randomUUID(), "Verne", "Jules")
}

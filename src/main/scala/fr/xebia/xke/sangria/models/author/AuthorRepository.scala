package fr.xebia.xke.sangria.models.author

import java.util.UUID


object AuthorRepository {

  val Author1 = Author(UUID.randomUUID(), "Dumas", "Alexandre")
  val Author2 = Author(UUID.randomUUID(), "Hugo", "Victor")

  private val repo = Seq(Author1, Author2)

  def author(firstName: String, lastName: String) = repo.find(author => author.firstName == firstName &&
    author.lastName == lastName)

  def authors = repo
}

package fr.xebia.xke.sangria.book

class BookRepository {
  private val repo = Seq(Book(1, "test1"), Book(2, "test2"))

  def book(id: Int) = repo.find(_.id == id)

  def books = repo
}

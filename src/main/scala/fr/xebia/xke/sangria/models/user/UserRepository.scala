package fr.xebia.xke.sangria.models.user


object UserRepository {

  val User1 = User("pg", Access.No)
  val User2 = User("charles", Access.Author)

  val Users = Seq(User1, User2)

  def authenticate(login: String, password: String): Option[User] =
    Users.find(_.login == login).filter(_ => password == "1234")

}

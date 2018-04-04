import sbt._

object Dependencies {
  lazy val circeCore = "io.circe" %% "circe-core" % "0.9.1"
  lazy val circeGeneric = "io.circe" %% "circe-generic" % "0.9.1"
  lazy val circeParser = "io.circe" %% "circe-parser" % "0.9.1"
  lazy val circeOptics = "io.circe" %% "circe-optics" % "0.9.1"

  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.0.11"
  lazy val sangria = "org.sangria-graphql" %% "sangria" % "1.3.3"

  lazy val sangriaCirce = "org.sangria-graphql" %% "sangria-circe" % "1.2.0"
  lazy val akkaHttpCirce = "de.heikoseeberger" %% "akka-http-circe" % "1.19.0"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4"
}

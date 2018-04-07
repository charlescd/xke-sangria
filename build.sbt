import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.4",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "Sangria",
    libraryDependencies ++= Seq(
      circeCore,
      circeGeneric,
      circeParser,
      circeOptics,

      akkaHttp,
      sangria,

      sangriaCirce,
      akkaHttpCirce,

      jwt,

      scalaTest % Test
    )
  )

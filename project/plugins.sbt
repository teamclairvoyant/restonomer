val scalafmtVersion = "2.4.6"
val assemblyVersion = "1.2.0"
val scalafixVersion = "0.11.0"
val wartremoverVersion = "3.1.3"

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % scalafmtVersion)
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % assemblyVersion)
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % scalafixVersion)
addSbtPlugin("org.wartremover" % "sbt-wartremover" % wartremoverVersion)

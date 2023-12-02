val assemblyVersion = "1.2.0"
val scalafixVersion = "0.11.0"
val scalafmtVersion = "2.4.6"
val wartremoverVersion = "3.1.5"

val assemblyPluginDependency = "com.eed3si9n" % "sbt-assembly" % assemblyVersion
val scalafixPluginDependency = "ch.epfl.scala" % "sbt-scalafix" % scalafixVersion
val scalafmtPluginDependency = "org.scalameta" % "sbt-scalafmt" % scalafmtVersion
val wartRemoverPluginDependency = "org.wartremover" % "sbt-wartremover" % wartremoverVersion

addSbtPlugin(assemblyPluginDependency)
addSbtPlugin(scalafixPluginDependency)
addSbtPlugin(scalafmtPluginDependency)
addSbtPlugin(wartRemoverPluginDependency)

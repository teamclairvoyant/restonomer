val assemblyVersion = "2.1.5"
val scalafixVersion = "0.11.1"
val scalafmtVersion = "2.5.2"
val wartremoverVersion = "3.1.6"

val assemblyPluginDependency = "com.eed3si9n" % "sbt-assembly" % assemblyVersion
val scalafixPluginDependency = "ch.epfl.scala" % "sbt-scalafix" % scalafixVersion
val scalafmtPluginDependency = "org.scalameta" % "sbt-scalafmt" % scalafmtVersion
val wartRemoverPluginDependency = "org.wartremover" % "sbt-wartremover" % wartremoverVersion

addSbtPlugin(assemblyPluginDependency)
addSbtPlugin(scalafixPluginDependency)
addSbtPlugin(scalafmtPluginDependency)
addSbtPlugin(wartRemoverPluginDependency)

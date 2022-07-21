// ----- VARIABLES ----- //

val organization = "com.clairvoyant.restonomer"
val name = "restonomer"
val version = "1.0-SNAPSHOT"

val scalaVersion = "3.1.2"
val sttpVersion = "3.7.1"

// ----- DEPENDENCIES ----- //

val sttpDependencies = Seq(
  "com.softwaremill.sttp.client3" %% "core" % sttpVersion
)

val commonDependencies = sttpDependencies

// ----- SETTINGS ----- //

val rootSettings = Seq(
  Keys.organization := organization,
  Keys.name := name,
  Keys.version := version
)

val commonSettings = Seq(
  Keys.scalaVersion := scalaVersion,
  libraryDependencies ++= commonDependencies
)

// ----- ROOT ----- //

lazy val restonomer = (project in file("."))
  .settings(rootSettings)
  .settings(commonSettings)

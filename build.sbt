// ----- VARIABLES ----- //

val organizationName = "com.clairvoyant.restonomer"
val applicationName = "restonomer"
val releaseVersion = "1.0"

val scalaVersion = "3.1.2"
val sttpVersion = "3.7.1"

// ----- DEPENDENCIES ----- //

val sttpDependencies = Seq(
  "com.softwaremill.sttp.client3" %% "core" % sttpVersion
)

val commonDependencies = sttpDependencies

// ----- SETTINGS ----- //

val rootSettings = Seq(
  organization := organizationName,
  name := applicationName,
  version := releaseVersion
)

val commonSettings = Seq(
  Keys.scalaVersion := scalaVersion,
  libraryDependencies ++= commonDependencies
)

// ----- PROJECTS ----- //

lazy val core = project in file("core")

lazy val app = project in file("core/app")

lazy val restonomer = (project in file("."))
  .settings(rootSettings)
  .settings(commonSettings)

// ----- VARIABLES ----- //

val organizationName = "com.clairvoyant.restonomer"
val applicationName = "restonomer"
val releaseVersion = "1.0"

val scalaVersion = "2.13.8"

val sttpVersion = "3.7.1"
val pureConfigVersion = "0.17.1"

// ----- DEPENDENCIES ----- //

val appDependencies = Seq(
  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
)

// ----- SETTINGS ----- //

val rootSettings = Seq(
  organization := organizationName,
  name := applicationName,
  version := releaseVersion
)

val commonSettings = Seq(
  Keys.scalaVersion := scalaVersion
)

val appSettings = Seq(
  libraryDependencies ++= appDependencies
)

// ----- PROJECTS ----- //

lazy val core = project in file("core")

lazy val app = (project in file("core/app"))
  .settings(appSettings)
  .dependsOn(model, exceptions, common)

lazy val common = project in file("core/common")

lazy val exceptions = project in file("core/exceptions")

lazy val model = project in file("core/model")

lazy val restonomer = (project in file("."))
  .settings(rootSettings)
  .settings(commonSettings)

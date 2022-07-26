ThisBuild / scalaVersion := "2.13.8"

// ----- VARIABLES ----- //

val organizationName = "com.clairvoyant.restonomer"
val applicationName = "restonomer"
val releaseVersion = "1.0"

val pureConfigVersion = "0.17.1"
val sttpVersion = "3.7.1"

// ----- TOOL DEPENDENCIES ----- //

val pureConfigDependencies = Seq(
  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
)

val sttpDependencies = Seq("com.softwaremill.sttp.client3" %% "core" % sttpVersion)

// ----- MODULE DEPENDENCIES ----- //

val appDependencies = pureConfigDependencies ++ sttpDependencies

val httpDependencies = sttpDependencies

val modelDependencies = pureConfigDependencies

// ----- SETTINGS ----- //

val rootSettings = Seq(
  organization := organizationName,
  name := applicationName,
  version := releaseVersion
)

val appSettings = Seq(
  libraryDependencies ++= appDependencies
)

val httpSettings = Seq(
  libraryDependencies ++= httpDependencies
)

val modelSettings = Seq(
  libraryDependencies ++= modelDependencies
)

// ----- PROJECTS ----- //

lazy val app = (project in file("core/app"))
  .settings(appSettings)
  .dependsOn(model, exceptions, common, http)

lazy val common = project in file("core/common")

lazy val exceptions = project in file("core/exceptions")

lazy val http = (project in file("core/http"))
  .settings(httpSettings)
  .dependsOn(model)

lazy val model = (project in file("core/model"))
  .settings(modelSettings)

lazy val restonomer = (project in file("."))
  .settings(rootSettings)

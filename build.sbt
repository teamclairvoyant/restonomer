ThisBuild / scalaVersion := "2.13.8"

// ----- VARIABLES ----- //

val organizationName = "com.clairvoyant.restonomer"
val applicationName = "restonomer"
val releaseVersion = "1.0"

val pureConfigVersion = "0.17.1"

// ----- TOOL DEPENDENCIES ----- //
val pureConfigDependencies = Seq(
  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
)

// ----- MODULE DEPENDENCIES ----- //

val appDependencies = pureConfigDependencies

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

val modelSettings = Seq(
  libraryDependencies ++= modelDependencies
)

// ----- PROJECTS ----- //

lazy val core = project in file("core")

lazy val app = (project in file("core/app"))
  .settings(appSettings)
  .dependsOn(model, exceptions, common)

lazy val common = project in file("core/common")

lazy val exceptions = project in file("core/exceptions")

lazy val model = (project in file("core/model"))
  .settings(modelSettings)

lazy val restonomer = (project in file("."))
  .settings(rootSettings)

ThisBuild / scalaVersion := "2.13.8"

// ----- VARIABLES ----- //

val organizationName = "com.clairvoyant.restonomer"
val applicationName = "restonomer"
val releaseVersion = "1.0"

val pureConfigVersion = "0.17.1"
val sttpVersion = "3.7.1"
val nScalaTimeVersion = "2.30.0"

// ----- TOOL DEPENDENCIES ----- //

val pureConfigDependencies = Seq(
  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
)

val sttpDependencies = Seq("com.softwaremill.sttp.client3" %% "core" % sttpVersion)

val nScalaTimeDependencies = Seq("com.github.nscala-time" %% "nscala-time" % nScalaTimeVersion)

// ----- MODULE DEPENDENCIES ----- //

val appDependencies = pureConfigDependencies ++ sttpDependencies ++ nScalaTimeDependencies

val commonDependencies = pureConfigDependencies ++ sttpDependencies

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

val commonSettings = Seq(
  libraryDependencies ++= commonDependencies
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
  .dependsOn(authentication, common, exceptions, model, http)

lazy val authentication = (project in file("core/authentication"))
  .dependsOn(model)

lazy val common = (project in file("core/common"))
  .settings(commonSettings)
  .dependsOn(exceptions)

lazy val exceptions = project in file("core/exceptions")

lazy val http = (project in file("core/http"))
  .settings(httpSettings)
  .dependsOn(model, exceptions, common, authentication)

lazy val model = (project in file("core/model"))
  .settings(modelSettings)
  .dependsOn(common)

lazy val restonomer = (project in file("."))
  .settings(rootSettings)

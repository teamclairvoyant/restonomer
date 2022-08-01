ThisBuild / scalaVersion := "2.13.8"

// ----- VARIABLES ----- //

val organizationName = "com.clairvoyant.restonomer"
val applicationName = "restonomer"
val releaseVersion = "1.0"

val pureConfigVersion = "0.17.1"
val sttpVersion = "3.7.1"
val nScalaTimeVersion = "2.30.0"
val scalaTestVersion = "3.2.12"

// ----- TOOL DEPENDENCIES ----- //

val pureConfigDependencies = Seq(
  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
)

val sttpDependencies = Seq("com.softwaremill.sttp.client3" %% "core" % sttpVersion)

val scalaTestDependencies = Seq("org.scalatest" %% "scalatest" % scalaTestVersion % Test)

// ----- MODULE DEPENDENCIES ----- //

val appDependencies = pureConfigDependencies ++ sttpDependencies

val authenticationDependencies = sttpDependencies

val backendDependencies = sttpDependencies

val commonDependencies = pureConfigDependencies ++ sttpDependencies ++ scalaTestDependencies

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

val authenticationSettings = Seq(
  libraryDependencies ++= authenticationDependencies
)

val backendSettings = Seq(
  libraryDependencies ++= backendDependencies
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
  .settings(authenticationSettings)
  .dependsOn(common, exceptions, model)

lazy val common = (project in file("core/common"))
  .settings(commonSettings)
  .dependsOn(exceptions, model)

lazy val exceptions = project in file("core/exceptions")

lazy val http = (project in file("core/http"))
  .settings(httpSettings)
  .dependsOn(authentication, common, exceptions, model)

lazy val model = (project in file("core/model"))
  .settings(modelSettings)

lazy val restonomer = (project in file("."))
  .settings(rootSettings)

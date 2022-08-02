ThisBuild / scalaVersion := "2.13.8"

// ----- VARIABLES ----- //

val organizationName = "com.clairvoyant.restonomer"
val applicationName = "restonomer"
val releaseVersion = "1.0"

val pureConfigVersion = "0.17.1"
val sttpVersion = "3.7.1"
val scalaTestVersion = "3.2.12"

// ----- TOOL DEPENDENCIES ----- //

val pureConfigDependencies = Seq("com.github.pureconfig" %% "pureconfig" % pureConfigVersion)

val sttpDependencies = Seq("com.softwaremill.sttp.client3" %% "core" % sttpVersion)

val scalaTestDependencies = Seq("org.scalatest" %% "scalatest" % scalaTestVersion % Test)

// ----- MODULE DEPENDENCIES ----- //

val coreDependencies = pureConfigDependencies ++ sttpDependencies ++ scalaTestDependencies

// ----- SETTINGS ----- //

val rootSettings = Seq(
  organization := organizationName,
  name := applicationName,
  version := releaseVersion
)

val coreSettings = Seq(
  libraryDependencies ++= coreDependencies
)

// ----- PROJECTS ----- //

lazy val core = (project in file("core"))
  .settings(coreSettings)

lazy val restonomer = (project in file("."))
  .settings(rootSettings)

ThisBuild / scalaVersion := "2.13.8"

inThisBuild(
  List(
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)

// ----- VARIABLES ----- //

val organizationName = "com.clairvoyant.restonomer"
val applicationName = "restonomer"
val releaseVersion = "1.0"

val pureConfigVersion = "0.17.1"
val sttpVersion = "3.7.2"
val scalaTestVersion = "3.2.12"
val wireMockVersion = "2.27.2"

lazy val scalacOptions = Seq("-Wunused")

// ----- TOOL DEPENDENCIES ----- //

val pureConfigDependencies = Seq("com.github.pureconfig" %% "pureconfig" % pureConfigVersion)

val sttpDependencies = Seq("com.softwaremill.sttp.client3" %% "core" % sttpVersion)

val scalaTestDependencies = Seq("org.scalatest" %% "scalatest" % scalaTestVersion % Test)

val wireMockDependencies = Seq("com.github.tomakehurst" % "wiremock-standalone" % wireMockVersion % Test)

// ----- MODULE DEPENDENCIES ----- //

val coreDependencies = pureConfigDependencies ++ sttpDependencies ++ scalaTestDependencies ++ wireMockDependencies

// ----- SETTINGS ----- //

val commonSettings = Seq(
  Keys.scalacOptions ++= scalacOptions
)

val rootSettings =
  commonSettings ++ Seq(
    organization := organizationName,
    name := applicationName,
    version := releaseVersion
  )

val coreSettings =
  commonSettings ++ Seq(
    libraryDependencies ++= coreDependencies
  )

// ----- PROJECTS ----- //

lazy val restonomer = (project in file("."))
  .settings(rootSettings)
  .aggregate(core)

lazy val core = (project in file("core"))
  .configs(IntegrationTest)
  .settings(coreSettings)

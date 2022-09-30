ThisBuild / scalaVersion := "2.13.8"

inThisBuild(
  List(
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)

Global / excludeLintKeys += Keys.parallelExecution

// ----- VARIABLES ----- //

val organizationName = "com.clairvoyant"
val applicationName = "restonomer"
val releaseVersion = "1.0"

val pureConfigVersion = "0.17.1"
val sttpVersion = "3.8.0"
val scalaTestVersion = "3.2.12"
val wireMockVersion = "2.27.2"
val jwtCoreVersion = "9.1.1"
val sparkVersion = "3.3.0"

lazy val scalacOptions = Seq("-Wunused")

// ----- TOOL DEPENDENCIES ----- //

val pureConfigDependencies = Seq("com.github.pureconfig" %% "pureconfig" % pureConfigVersion)

val sttpDependencies = Seq("com.softwaremill.sttp.client3" %% "core" % sttpVersion)

val scalaTestDependencies = Seq("org.scalatest" %% "scalatest" % scalaTestVersion % "it,test")

val wireMockDependencies = Seq("com.github.tomakehurst" % "wiremock-standalone" % wireMockVersion % "it,test")

val jwtDependency = Seq("com.github.jwt-scala" %% "jwt-core" % jwtCoreVersion)
// ----- MODULE DEPENDENCIES ----- //

val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)

// ----- MODULE DEPENDENCIES ----- //

val coreDependencies =
  pureConfigDependencies ++ sttpDependencies ++ scalaTestDependencies ++ wireMockDependencies ++ sparkDependencies ++ jwtDependency


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
    libraryDependencies ++= coreDependencies,
    IntegrationTest / parallelExecution := false
  ) ++ Defaults.itSettings

// ----- PROJECTS ----- //

lazy val restonomer = (project in file("."))
  .settings(rootSettings)
  .aggregate(core)

lazy val core = (project in file("core"))
  .configs(IntegrationTest)
  .settings(coreSettings)

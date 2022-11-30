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

val pureConfigVersion = "0.17.2"
val sttpVersion = "3.8.3"
val scalaTestVersion = "3.2.14"
val wireMockVersion = "2.27.2"
val jwtCoreVersion = "9.1.2"
val sparkVersion = "3.3.0"
val catsVersion = "2.9.0"
val json4sJacksonVersion = "4.0.6"
val retryVersion = "0.3.6"

lazy val scalacOptions = Seq("-Wunused")

// ----- TOOL DEPENDENCIES ----- //

val pureConfigDependencies = Seq("com.github.pureconfig" %% "pureconfig" % pureConfigVersion)

val sttpDependencies = Seq("com.softwaremill.sttp.client3" %% "core" % sttpVersion)

val scalaTestDependencies = Seq("org.scalatest" %% "scalatest" % scalaTestVersion)

val wireMockDependencies = Seq("com.github.tomakehurst" % "wiremock-standalone" % wireMockVersion % "it,test")

val jwtDependencies = Seq("com.github.jwt-scala" %% "jwt-core" % jwtCoreVersion)

val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)

val catsDependencies = Seq("org.typelevel" %% "cats-core" % catsVersion)

val retryDependencies = Seq("com.softwaremill.retry" %% "retry" % retryVersion)

// ----- MODULE DEPENDENCIES ----- //

val coreDependencies =
  pureConfigDependencies ++
    sttpDependencies ++
    jwtDependencies ++
    retryDependencies ++
    scalaTestDependencies.map(_ % "it,test") ++
    wireMockDependencies

val sparkUtilsDependencies =
  sparkDependencies ++
    catsDependencies ++
    scalaTestDependencies.map(_ % "test")

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
    Test / parallelExecution := false,
    IntegrationTest / parallelExecution := false
  ) ++ Defaults.itSettings

val sparkUtilsSettings =
  commonSettings ++ Seq(
    libraryDependencies ++= sparkUtilsDependencies
  )

// ----- PROJECTS ----- //

lazy val restonomer = (project in file("."))
  .settings(rootSettings)
  .aggregate(core, `spark-utils`)

lazy val core = (project in file("core"))
  .configs(IntegrationTest)
  .settings(coreSettings)
  .dependsOn(`spark-utils` % "compile->compile;test->test;it->it;test->it")

lazy val `spark-utils` = (project in file("spark-utils"))
  .configs(IntegrationTest.extend(Test))
  .settings(sparkUtilsSettings)

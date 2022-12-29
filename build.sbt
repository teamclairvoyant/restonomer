ThisBuild / scalaVersion := "2.13.8"

inThisBuild(
  List(
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)

Global / excludeLintKeys += Keys.parallelExecution

// ----- VARIABLES ----- //

val organizationName = "com.clairvoyant.restonomer"
val releaseVersion = "1.0"

val pureConfigVersion = "0.17.2"
val sttpVersion = "3.8.5"
val akkaBackendVersion = "3.8.5"
val akkaStreamVersion = "2.8.0-M1"
val scalaTestVersion = "3.2.14"
val wireMockVersion = "2.27.2"
val jwtCoreVersion = "9.1.2"
val sparkVersion = "3.3.0"
val catsVersion = "2.9.0"
val jsonPathVersion = "2.7.0"

lazy val scalacOptions = Seq("-Wunused")

// ----- TOOL DEPENDENCIES ----- //

val pureConfigDependencies = Seq("com.github.pureconfig" %% "pureconfig" % pureConfigVersion)

val sttpDependencies = Seq("com.softwaremill.sttp.client3" %% "core" % sttpVersion)

val akkaBackendDependencies = Seq(
  "com.softwaremill.sttp.client3" %% "akka-http-backend" % akkaBackendVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaStreamVersion
)

val scalaTestDependencies = Seq("org.scalatest" %% "scalatest" % scalaTestVersion)

val wireMockDependencies = Seq("com.github.tomakehurst" % "wiremock-standalone" % wireMockVersion % "it,test")

val jwtDependencies = Seq("com.github.jwt-scala" %% "jwt-core" % jwtCoreVersion)

val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)

val catsDependencies = Seq("org.typelevel" %% "cats-core" % catsVersion)

val jsonPathDependencies = Seq("com.jayway.jsonpath" % "json-path" % jsonPathVersion)

// ----- MODULE DEPENDENCIES ----- //

val restonomerCoreDependencies =
  pureConfigDependencies ++
    sttpDependencies ++
    akkaBackendDependencies ++
    jwtDependencies ++
    jsonPathDependencies ++
    scalaTestDependencies.map(_ % "it,test") ++
    wireMockDependencies

val restonomerSparkUtilsDependencies =
  sparkDependencies ++
    catsDependencies ++
    scalaTestDependencies.map(_ % "test")

// ----- SETTINGS ----- //

val commonSettings = Seq(
  organization := organizationName,
  version := releaseVersion,
  Keys.scalacOptions ++= scalacOptions
)

val restonomerCoreSettings =
  commonSettings ++ Seq(
    libraryDependencies ++= restonomerCoreDependencies,
    Test / parallelExecution := false,
    IntegrationTest / parallelExecution := false
  ) ++ Defaults.itSettings

val restonomerSparkUtilsSettings =
  commonSettings ++ Seq(
    libraryDependencies ++= restonomerSparkUtilsDependencies
  )

// ----- PROJECTS ----- //

lazy val restonomer = (project in file("."))
  .settings(commonSettings)
  .aggregate(`restonomer-core`, `restonomer-spark-utils`)

lazy val `restonomer-core` = (project in file("restonomer-core"))
  .configs(IntegrationTest)
  .settings(restonomerCoreSettings)
  .dependsOn(`restonomer-spark-utils` % "compile->compile;test->test;it->it;test->it")

lazy val `restonomer-docs` = project in file("restonomer-docs")

lazy val `restonomer-spark-utils` = (project in file("restonomer-spark-utils"))
  .configs(IntegrationTest.extend(Test))
  .settings(restonomerSparkUtilsSettings)

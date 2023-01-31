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
val sttpVersion = "3.8.9"
val scalaTestVersion = "3.2.15"
val wireMockVersion = "2.27.2"
val jwtCoreVersion = "9.1.2"
val sparkVersion = "3.3.0"
val catsVersion = "2.9.0"
val jsonPathVersion = "2.7.0"
val odelayVersion = "0.4.0"

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

val jsonPathDependencies = Seq("com.jayway.jsonpath" % "json-path" % jsonPathVersion)

val odelayDependencies = Seq("com.softwaremill.odelay" %% "odelay-core" % odelayVersion)

// ----- MODULE DEPENDENCIES ----- //

val restonomerCoreDependencies =
  pureConfigDependencies ++
    sttpDependencies ++
    jwtDependencies ++
    jsonPathDependencies ++
    scalaTestDependencies.map(_ % "it,test") ++
    wireMockDependencies ++
    odelayDependencies

val restonomerSparkUtilsDependencies =
  sparkDependencies ++
    catsDependencies ++
    scalaTestDependencies.map(_ % "test")

// ----- SETTINGS ----- //

val commonSettings = Seq(
  organization := organizationName,
  version := releaseVersion,
  Keys.scalacOptions ++= scalacOptions,
  githubOwner := "teamclairvoyant",
  githubRepository := "restonomer",
  githubTokenSource := TokenSource.GitConfig("github.token")
)

val restonomerCoreSettings =
  commonSettings ++ Seq(
    libraryDependencies ++= restonomerCoreDependencies,
    Test / parallelExecution := false,
    IntegrationTest / parallelExecution := false
  ) ++ Defaults.itSettings

val restonomerDocsSettings = commonSettings

val restonomerSparkUtilsSettings =
  commonSettings ++ Seq(
    libraryDependencies ++= restonomerSparkUtilsDependencies
  )

// ----- PROJECTS ----- //

lazy val restonomer = (project in file("."))
  .settings(commonSettings)
  .aggregate(`restonomer-core`, `restonomer-spark-utils`)

lazy val `restonomer-core` = project
  .configs(IntegrationTest)
  .settings(restonomerCoreSettings)
  .dependsOn(`restonomer-spark-utils` % "compile->compile;test->test;it->it;test->it")

lazy val `restonomer-docs` = project
  .settings(restonomerDocsSettings)

lazy val `restonomer-spark-utils` = project
  .configs(IntegrationTest.extend(Test))
  .settings(restonomerSparkUtilsSettings)

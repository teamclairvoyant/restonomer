ThisBuild / scalaVersion := "3.2.2"

Global / excludeLintKeys += Keys.parallelExecution

lazy val scalacOptions = Seq("-Xmax-inlines", "50")

// ----- VARIABLES ----- //

val organizationName = "com.clairvoyant.restonomer"
val releaseVersion = "2.0"

val zioConfigVersion = "4.0.0-RC14"
val sttpVersion = "3.8.13"
val scalaTestVersion = "3.2.15"
val wireMockVersion = "2.27.2"
val jwtCoreVersion = "9.2.0"
val sparkVersion = "3.3.2"
val catsVersion = "2.9.0"
val jsonPathVersion = "2.7.0"
val odelayVersion = "0.4.0"
val s3MockVersion = "0.2.6"
val scalaXmlVersion = "2.1.0"
val scalaParserCombinatorsVersion = "2.2.0"

// ----- TOOL DEPENDENCIES ----- //

val zioConfigDependencies = Seq(
  "dev.zio" %% "zio-config" % zioConfigVersion,
  "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,
  "dev.zio" %% "zio-config-typesafe" % zioConfigVersion
)

val sttpDependencies = Seq("com.softwaremill.sttp.client3" %% "core" % sttpVersion)

val scalaTestDependencies = Seq("org.scalatest" %% "scalatest" % scalaTestVersion)

val wireMockDependencies = Seq("com.github.tomakehurst" % "wiremock-standalone" % wireMockVersion % "it,test")

val jwtDependencies = Seq("com.github.jwt-scala" %% "jwt-core" % jwtCoreVersion)

val scalaXmlDependencies = Seq("org.scala-lang.modules" %% "scala-xml" % scalaXmlVersion)

val scalaParserCombinatorsDependencies = Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % scalaParserCombinatorsVersion
)

val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-hadoop-cloud" % sparkVersion
)
  .map(_ excludeAll ("org.scala-lang.modules", "scala-xml"))
  .map(_.cross(CrossVersion.for3Use2_13))

val catsDependencies = Seq("org.typelevel" %% "cats-core" % catsVersion)

val jsonPathDependencies = Seq("com.jayway.jsonpath" % "json-path" % jsonPathVersion)

val odelayDependencies = Seq("com.softwaremill.odelay" %% "odelay-core" % odelayVersion)

val s3MockDependencies = Seq("io.findify" %% "s3mock" % s3MockVersion % "it,test")
  .map(_ excludeAll ("org.scala-lang.modules", "scala-collection-compat"))
  .map(_.cross(CrossVersion.for3Use2_13))

// ----- MODULE DEPENDENCIES ----- //

val restonomerCoreDependencies =
  zioConfigDependencies ++
    scalaXmlDependencies ++
    scalaParserCombinatorsDependencies ++
    sttpDependencies ++
    jwtDependencies ++
    jsonPathDependencies ++
    scalaTestDependencies.map(_ % "it,test") ++
    wireMockDependencies ++
    s3MockDependencies ++
    odelayDependencies

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
  .settings(
    commonSettings ++ Seq(
      publish := {},
      publishLocal := {}
    )
  )
  .aggregate(`restonomer-core`, `restonomer-spark-utils`)

lazy val `restonomer-core` = project
  .configs(IntegrationTest)
  .settings(restonomerCoreSettings)
  .dependsOn(`restonomer-spark-utils` % "compile->compile;test->test;it->it;test->it")

lazy val `restonomer-spark-utils` = project
  .configs(IntegrationTest.extend(Test))
  .settings(restonomerSparkUtilsSettings)

// ----- PUBLISH TO GITHUB PACKAGES ----- //

ThisBuild / publishTo := Some("Restonomer Github Repo" at "https://maven.pkg.github.com/teamclairvoyant/restonomer/")

credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  "teamclairvoyant",
  System.getenv("GITHUB_TOKEN")
)

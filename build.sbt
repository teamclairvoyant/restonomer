ThisBuild / scalaVersion := "3.3.0"

ThisBuild / credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  System.getenv("GITHUB_USERNAME"),
  System.getenv("GITHUB_TOKEN")
)

// ----- RESOLVERS ----- //

ThisBuild / resolvers ++= Seq(
  "DataScalaxyReaderText Repo" at "https://maven.pkg.github.com/teamclairvoyant/data-scalaxy-reader-text/",
  "DataScalaxyTestUtil Repo" at "https://maven.pkg.github.com/teamclairvoyant/data-scalaxy-test-util/",
  "DataScalaxyTransformer Repo" at "https://maven.pkg.github.com/teamclairvoyant/data-scalaxy-transformer/",
  "DataScalaxyWriter Repo" at "https://maven.pkg.github.com/teamclairvoyant/data-scalaxy-writer/"
)

// ----- PACKAGE SETTINGS ----- //

ThisBuild / organization := "com.clairvoyant"

ThisBuild / version := "2.2.0"

// ----- PUBLISH TO GITHUB PACKAGES ----- //

ThisBuild / publishTo := Some("Restonomer Github Repo" at "https://maven.pkg.github.com/teamclairvoyant/restonomer/")

// ----- ASSEMBLY MERGE STRATEGY ----- //

ThisBuild / assemblyMergeStrategy := {
  case PathList(ps @ _*)
      if (ps.last endsWith "io.netty.versions.properties")
        || (ps.last endsWith "reflection-config.json")
        || (ps.last endsWith "native-image.properties")
        || (ps.last endsWith "module-info.class")
        || (ps.last endsWith "UnusedStubClass.class") =>
    MergeStrategy.last
  case PathList(ps @ _*) if ps.last endsWith "public-suffix-list.txt" => MergeStrategy.concat
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}

// ----- SCALA COMPILER OPTIONS ----- //

Global / scalacOptions ++= Seq("-Xmax-inlines", "50")

// ----- SCALAFIX ----- //

ThisBuild / semanticdbEnabled := true
ThisBuild / scalafixOnCompile := true

// ----- WARTREMOVER ----- //

ThisBuild / wartremoverErrors ++= Warts.allBut(
  Wart.Any,
  Wart.DefaultArguments,
  Wart.Equals,
  Wart.FinalCaseClass,
  Wart.GlobalExecutionContext,
  Wart.ImplicitParameter,
  Wart.IsInstanceOf,
  Wart.IterableOps,
  Wart.LeakingSealed,
  Wart.Nothing,
  Wart.OptionPartial,
  Wart.Overloading,
  Wart.PlatformDefault,
  Wart.Recursion,
  Wart.RedundantConversions,
  Wart.StringPlusAny,
  Wart.Throw,
  Wart.ToString
)

// ----- TOOL VERSIONS ----- //

val dataScalaxyReaderTextVersion = "1.0.0"
val dataScalaxyTestUtilVersion = "1.0.0"
val dataScalaxyTransformerVersion = "1.0.0"
val dataScalaxyWriterVersion = "1.0.0"
val googleCloudStorageVersion = "2.26.1"
val jsonPathVersion = "2.8.0"
val jwtCoreVersion = "9.4.3"
val monovoreDeclineVersion = "2.4.1"
val odelayVersion = "0.4.0"
val scalaParserCombinatorsVersion = "2.3.0"
val sttpVersion = "3.9.0"
val testContainersScalaVersion = "0.40.17"
val wireMockVersion = "2.27.2"
val zioConfigVersion = "4.0.0-RC16"

// ----- TOOL DEPENDENCIES ----- //

val dataScalaxyReaderTextDependencies = Seq(
  "com.clairvoyant.data.scalaxy" %% "text-reader" % dataScalaxyReaderTextVersion
).map(_ excludeAll ("org.scala-lang.modules", "scala-collection-compat"))

val dataScalaxyTestUtilDependencies = Seq(
  "com.clairvoyant.data.scalaxy" %% "test-util" % dataScalaxyTestUtilVersion % "it,test"
)

val dataScalaxyTransformerDependencies = Seq(
  "com.clairvoyant.data.scalaxy" %% "transformer" % dataScalaxyTestUtilVersion
).map(_ excludeAll ("org.scala-lang.modules", "scala-collection-compat"))

val dataScalaxyWriterDependencies = Seq(
  "com.clairvoyant.data.scalaxy" %% "writer-local-file-system" % dataScalaxyWriterVersion,
  "com.clairvoyant.data.scalaxy" %% "writer-aws" % dataScalaxyWriterVersion,
  "com.clairvoyant.data.scalaxy" %% "writer-gcp" % dataScalaxyWriterVersion
).map(_ excludeAll ("org.scala-lang.modules", "scala-collection-compat"))

val googleCloudStorageDependencies = Seq("com.google.cloud" % "google-cloud-storage" % googleCloudStorageVersion)

val jsonPathDependencies = Seq("com.jayway.jsonpath" % "json-path" % jsonPathVersion)

val jwtCoreDependencies = Seq("com.github.jwt-scala" %% "jwt-core" % jwtCoreVersion)

val monovoreDeclineDependencies = Seq("com.monovore" %% "decline" % monovoreDeclineVersion)

val odelayDependencies = Seq("com.softwaremill.odelay" %% "odelay-core" % odelayVersion)

val scalaParserCombinatorsDependencies = Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % scalaParserCombinatorsVersion
)

val sttpDependencies = Seq("com.softwaremill.sttp.client3" %% "core" % sttpVersion)

val testContainersScalaDependencies = Seq("com.dimafeng" %% "testcontainers-scala" % testContainersScalaVersion % Test)

val wireMockDependencies = Seq("com.github.tomakehurst" % "wiremock-standalone" % wireMockVersion % "it,test")

val zioConfigDependencies = Seq(
  "dev.zio" %% "zio-config" % zioConfigVersion,
  "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,
  "dev.zio" %% "zio-config-typesafe" % zioConfigVersion
)

// ----- MODULE DEPENDENCIES ----- //

val restonomerDependencies =
  dataScalaxyReaderTextDependencies ++
    dataScalaxyTestUtilDependencies ++
    dataScalaxyTransformerDependencies ++
    dataScalaxyWriterDependencies ++
    googleCloudStorageDependencies ++
    jsonPathDependencies ++
    jwtCoreDependencies ++
    monovoreDeclineDependencies ++
    odelayDependencies ++
    scalaParserCombinatorsDependencies ++
    sttpDependencies ++
    testContainersScalaDependencies ++
    wireMockDependencies ++
    zioConfigDependencies

// ----- PROJECTS ----- //

lazy val restonomer = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    libraryDependencies ++= restonomerDependencies,
    Test / parallelExecution := false,
    IntegrationTest / parallelExecution := false,
    Defaults.itSettings,
    scalafixConfigSettings(IntegrationTest),
    assembly / mainClass := Some("com.clairvoyant.restonomer.app.RestonomerApp")
  )
  .enablePlugins(AssemblyPlugin)

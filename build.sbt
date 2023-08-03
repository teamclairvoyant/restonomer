ThisBuild / scalaVersion := "3.2.2"

Global / excludeLintKeys += Keys.parallelExecution

lazy val scalacOptions = Seq("-Xmax-inlines", "50")

// ----- VARIABLES ----- //

val organizationName = "com.clairvoyant.restonomer"
val releaseVersion = "2.2.0"

val zioConfigVersion = "4.0.0-RC14"
val sttpVersion = "3.8.16"
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
val gcsConnectorVersion = "hadoop3-2.2.2"
val monovoreDeclineVersion = "2.4.1"
val googleCloudStorageVersion = "2.24.0"
val testContainersScalaVersion = "0.40.17"

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
  "org.apache.spark" %% "spark-sql" % sparkVersion
)
  .map(_ excludeAll ("org.scala-lang.modules", "scala-xml"))
  .map(_.cross(CrossVersion.for3Use2_13))
  .map(_ % "provided")

val sparkHadoopCloudDependencies = Seq("org.apache.spark" %% "spark-hadoop-cloud" % sparkVersion)
  .map(_ exclude ("org.apache.hadoop", "hadoop-client-runtime"))
  .map(_.cross(CrossVersion.for3Use2_13))

val catsDependencies = Seq("org.typelevel" %% "cats-core" % catsVersion)

val jsonPathDependencies = Seq("com.jayway.jsonpath" % "json-path" % jsonPathVersion)

val odelayDependencies = Seq("com.softwaremill.odelay" %% "odelay-core" % odelayVersion)

val s3MockDependencies = Seq("io.findify" %% "s3mock" % s3MockVersion % "it,test")
  .map(_ excludeAll ("org.scala-lang.modules", "scala-collection-compat"))
  .map(_.cross(CrossVersion.for3Use2_13))

val gcsConnectorDependencies = Seq("com.google.cloud.bigdataoss" % "gcs-connector" % gcsConnectorVersion)

val monovoreDeclineDependencies = Seq("com.monovore" %% "decline" % monovoreDeclineVersion)

val googleCloudStorageDependencies = Seq("com.google.cloud" % "google-cloud-storage" % googleCloudStorageVersion)

val testContainersScalaDependencies = Seq("com.dimafeng" %% "testcontainers-scala" % testContainersScalaVersion % Test)

// ----- MODULE DEPENDENCIES ----- //

val restonomerCoreDependencies =
  zioConfigDependencies ++
    scalaXmlDependencies ++
    scalaParserCombinatorsDependencies ++
    sparkDependencies ++
    sttpDependencies ++
    jwtDependencies ++
    jsonPathDependencies ++
    scalaTestDependencies.map(_ % "it,test") ++
    wireMockDependencies ++
    s3MockDependencies ++
    odelayDependencies ++
    gcsConnectorDependencies ++
    monovoreDeclineDependencies ++
    googleCloudStorageDependencies ++
    testContainersScalaDependencies

val restonomerSparkUtilsDependencies =
  sparkDependencies ++
    sparkHadoopCloudDependencies ++
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
    IntegrationTest / parallelExecution := false,
    assembly / mainClass := Some("com.clairvoyant.restonomer.core.app.RestonomerApp")
  ) ++ Defaults.itSettings

val restonomerSparkUtilsSettings =
  commonSettings ++ Seq(
    libraryDependencies ++= restonomerSparkUtilsDependencies
  )

// ----- PROJECTS ----- //

lazy val restonomer = (project in file("."))
  .settings(
    commonSettings ++ Seq(
      publish / skip := true,
      publishLocal / skip := true
    ),
    addCommandAlias("run", "restonomer-core/run")
  )
  .aggregate(`restonomer-core`, `restonomer-spark-utils`)

lazy val `restonomer-core` = project
  .configs(IntegrationTest)
  .settings(restonomerCoreSettings)
  .dependsOn(`restonomer-spark-utils` % "compile->compile;test->test;it->it;test->it")
  .enablePlugins(AssemblyPlugin)

lazy val `restonomer-spark-utils` = project
  .configs(IntegrationTest.extend(Test))
  .settings(restonomerSparkUtilsSettings)
  .enablePlugins(AssemblyPlugin)

// ----- PUBLISH TO GITHUB PACKAGES ----- //

ThisBuild / publishTo := Some("Restonomer Github Repo" at "https://maven.pkg.github.com/teamclairvoyant/restonomer/")

ThisBuild / credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  "teamclairvoyant",
  System.getenv("GITHUB_TOKEN")
)

// ----- ASSEMBLY MERGE STRATEGY ----- //

ThisBuild / assemblyMergeStrategy := {
  case PathList(ps @ _*)
      if (ps.last endsWith "io.netty.versions.properties")
        || (ps.last endsWith "reflection-config.json")
        || (ps.last endsWith "native-image.properties")
        || (ps.last endsWith "module-info.class")
        || (ps.last endsWith "UnusedStubClass.class") =>
    MergeStrategy.last
  case PathList(ps @ _*) if ps.last endsWith "public-suffix-list.txt" =>
    MergeStrategy.concat
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}

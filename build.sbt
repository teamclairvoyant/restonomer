ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.2"

val sttpVersion = "3.7.1"

val commonSettings = Seq(
  organization := "com.clairvoyant.restonomer"
)

lazy val restonomerRoot = (project in file("."))
  .settings(
    name := "restonomer"
  )

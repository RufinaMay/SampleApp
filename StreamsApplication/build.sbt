import sbt._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.12"

lazy val IntegrationTest = config("it") extend Test

lazy val root = (project in file("."))
  .enablePlugins(JavaServerAppPackaging)
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .settings(
    name := "StreamsApplication",
    libraryDependencies ++= Dependencies.allDependencies
  )

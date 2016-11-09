
name := """SamplePlay"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava, SbtWeb)
  .settings( pipelineStages := Seq(digest, gzip))

scalaVersion := "2.11.7"


//add javaJdbc for jdbc
libraryDependencies ++= Seq(
  cache,
  javaWs
)


//fork in run := true

fork in run := true
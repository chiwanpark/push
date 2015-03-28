organization := "com.chiwanpark"

name := "push"

version := "0.1.0"

scalaVersion := "2.11.6"

enablePlugins(JavaAppPackaging)

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= {
  val akkaVersion = "2.3.9"
  val sprayVersion = "1.3.3"
  val sprayJsonVersion = "1.3.1"

  Seq(
    // spray
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-testkit" % sprayVersion % "test",
    "io.spray" %% "spray-httpx" % sprayVersion,
    "io.spray" %% "spray-json" % sprayJsonVersion,

    // akka
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",

    // database
    "com.typesafe.slick" %% "slick" % "3.0.0-RC1",
    "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",

    // logging
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "ch.qos.logback" % "logback-classic" % "1.1.2",

    // test
    "org.specs2" %% "specs2-core" % "2.3.11" % "test"
  )
}

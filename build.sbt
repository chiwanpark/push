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

  Seq(
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-testkit" % sprayVersion % "test",
    "io.spray" %% "spray-httpx" % sprayVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.specs2" %% "specs2-core" % "2.3.11" % "test"
  )
}

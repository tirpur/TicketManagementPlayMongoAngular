name := """Issue Management System"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,

  specs2 % Test
)

libraryDependencies += "org.reactivemongo" %% "reactivemongo" % "0.11.7"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.2.play24")
//libraryDependencies += "org.reactivemongo" % "play2-reactivemongo_2.11" % "0.11.7.play24"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

herokuAppName in Compile := "gentle-lowlands-9363"

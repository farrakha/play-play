name := "food-trucks"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "uk.co.panaxiom" %% "play-jongo" % "0.6.0-jongo1.0",
  "com.socrata" % "soda-api-java" % "0.9.11"
)

play.Project.playJavaSettings

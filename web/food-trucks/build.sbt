name := "food-trucks"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "de.undercouch" % "bson4jackson" % "2.1.0" force(),
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.1.0" force(),
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.1.0" force(),
  "com.fasterxml.jackson.core" % "jackson-core" % "2.1.0" force(),
  "org.mongodb" % "mongo-java-driver" % "2.11.3",
  "org.jongo" % "jongo" % "1.0",
  "uk.co.panaxiom" %% "play-jongo" % "0.6.0-jongo1.0",
  "com.socrata" % "soda-api-java" % "0.9.11"
)

play.Project.playJavaSettings

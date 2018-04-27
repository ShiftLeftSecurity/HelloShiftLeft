
name := "helloshiftleft-play"
organization := "io.shiftleft"

version := "0.0.1-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

crossPaths := false

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  guice,
  javaJpa,
  evolutions,
  jdbc,
  ws,
  "org.hibernate" % "hibernate-entitymanager" % "5.2.12.Final",
  "org.projectlombok" % "lombok" % "1.16.18",
  "org.apache.commons" % "commons-io" % "1.3.2",
  "commons-collections" % "commons-collections" % "3.1",
  "org.springframework" % "spring-core" % "5.0.0.RELEASE",
  "org.springframework" % "spring-context" % "5.0.0.RELEASE",
  "org.springframework" % "spring-beans" % "5.0.0.RELEASE",
  "org.springframework" % "spring-expression" % "5.0.0.RELEASE",
)

// enforce slightly older jackson-databind, before the CVE-2017-7525 fix
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.5"
dependencyOverrides += "commons-beanutils" % "commons-beanutils" % "1.9.2"
dependencyOverrides += "commons-logging" % "commons-logging" % "1.2"




PlayKeys.devSettings := Seq("play.server.http.address" -> "127.0.0.1",
                            "play.server.http.port" -> "8082")

// Big standalone jar configuration (not officially supported by Play)

assembly/mainClass := Some("play.core.server.ProdServerStart")
assembly/fullClasspath += Attributed.blank(PlayKeys.playPackageAssets.value)

assembly/assemblyMergeStrategy := {
  case PathList("javax", "persistence", xs @ _*)     => MergeStrategy.last
  case PathList("javax", "transaction", xs @ _*)     => MergeStrategy.last
  case PathList("org", "apache", "commons", "logging", xs @ _*)     => MergeStrategy.last
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".sql"  => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case "play/reference-overrides.conf"               => MergeStrategy.first
  case x =>
    val oldStrategy = (assembly/assemblyMergeStrategy).value
    oldStrategy(x)
}

resolvers ++= Seq(
  ("Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/").withAllowInsecureProtocol(true),
  ("spray repo" at "http://repo.spray.io").withAllowInsecureProtocol(true),
  Classpaths.sbtPluginReleases
)

addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.2")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4")
addSbtPlugin("org.jetbrains" % "sbt-ide-settings" % "1.1.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.12")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")
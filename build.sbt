lazy val commonSettings = Defaults.coreDefaultSettings ++ Formatting.formatSettings ++ Seq(
  organization := "com.batusha",
  version := "1.0.0",
  scalaVersion := Dependencies.scalaLastVersion,
  scalacOptions ++= List("-unchecked", "-deprecation", "-encoding", "UTF8", "-feature")
)

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := "lightweight",
    libraryDependencies ++= Dependencies.core ++ Dependencies.test
  )

// this will add the ability to "stage" which is required for Heroku
enablePlugins(JavaAppPackaging)

// this specifies which class is the main class in the package
mainClass in Compile := Some("example.ExampleApi")

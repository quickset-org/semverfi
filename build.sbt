version := "1.0.0"
scalaVersion := "2.13.1"
moduleName         := "semverfi"
organization       := "org.quickset"
description := "Always Faithful, always loyal semantic versions"
homepage := Some(url("https://github.com/quickset-org/semverfi"))
scalacOptions ++= Seq(
	"-deprecation",                      // Emit warning and location for usages of deprecated APIs.
	"-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
	"-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
	"-language:implicitConversions",     // Allow definition of implicit functions called views
	"-language:postfixOps"
)
libraryDependencies ++= List(
  "com.lihaoyi" %% "fastparse" % "2.2.4",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"
)

publishMavenStyle := true
publishTo := Some(
	if (isSnapshot.value)
	Opts.resolver.sonatypeSnapshots
	else
	Opts.resolver.sonatypeStaging
)
publishArtifact in Test := false
licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
pomExtra := (
	<scm>
	<url>git@github.com:quickset-org/semverfi.git</url>
	<connection>scm:git:git@github.com:quickset-org/semverfi.git</connection>
	</scm>
	<developers>
	<developer>
		<id>quickset-org</id>
		<name>Quickset Org</name>
		<url>https://github.com/quickset-org</url>
	</developer>
	</developers>)
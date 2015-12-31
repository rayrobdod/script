import sbt._
import Keys._


object ScriptBuild extends Build {
	
	lazy val root = Project(
			id = "script",
			base = file("."),
			settings = Defaults.coreDefaultSettings
	)
}

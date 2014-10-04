import sbt._
import Keys._
import java.util.zip.{ZipInputStream, ZipOutputStream, ZipEntry}


object ScriptBuild extends Build {
	
	lazy val root = Project(
			id = "script",
			base = file("."),
			settings = Defaults.defaultSettings
	)
}
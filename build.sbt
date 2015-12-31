name := "game-script"

organization := "com.rayrobdod"

organizationHomepage := Some(new URL("http://rayrobdod.name/"))

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.6", "2.11.7")
//    (if (System.getProperty("scoverage.disable", "") != "true") {Nil} else {Seq("2.12.0-M3")})

mainClass in Compile := Some("com.rayrobdod.scriptSample.Main")

libraryDependencies += ("com.rayrobdod" %% "anti-xml" % "0.7-SNAPSHOT-20150909")



packageOptions in (Compile, packageBin) <+= (scalaVersion, sourceDirectory).map{(scalaVersion:String, srcDir:File) =>
	val manifest = new java.util.jar.Manifest(new java.io.FileInputStream(srcDir + "/main/MANIFEST.MF"))
	manifest.getAttributes("scala/").putValue("Implementation-Version", scalaVersion)
	Package.JarManifest( manifest )
}



javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.7", "-target", "1.7")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-target:jvm-1.7")

scalacOptions in doc in Compile ++= Seq(
		"-doc-title", name.value,
		"-doc-version", version.value,
		"-doc-root-content", ((scalaSource in Compile).value / "rootdoc.txt").toString,
		"-diagrams",
		"-sourcepath", baseDirectory.value.toString,
		"-doc-source-url", "https://github.com/rayrobdod/script/tree/" + version.value + "€{FILE_PATH}.scala"
)

autoAPIMappings in doc in Compile := true

excludeFilter in unmanagedSources in Compile := new FileFilter{
	def accept(n:File) = {
		val abPath = n.getAbsolutePath().replace('\\', '/')
		(
			(abPath endsWith "???")
		)
	}
}

scalastyleConfig := baseDirectory.value / "project" / "scalastyle-config.xml"


// scalaTest

libraryDependencies += "org.scalatest" %% "scalatest" % (
      "2.2.5" + (if ((scalaVersion.value take 7) == "2.12.0-") { "-" + (scalaVersion.value drop 7) } else {""}) 
    ) % "test"

testOptions in Test += Tests.Argument("-oS")


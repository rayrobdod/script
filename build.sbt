name := "game-script"

organization := "com.rayrobdod"

organizationHomepage := Some(new URL("http://rayrobdod.name/"))

version := "1.0.0-RC1"

scalaVersion := "2.11.2"

crossScalaVersions ++= Seq("2.9.1", "2.9.2", "2.9.3", "2.10.4", "2.11.2")

// exportJars := true

mainClass := Some("com.rayrobdod.scriptSample.Main")

libraryDependencies += ("no.arktekk" %% "anti-xml" % "[0.5.1,0.5.2]") cross CrossVersion.binaryMapped {
			case "2.9.3" => "2.9.2"
			case x => x
}



packageOptions in (Compile, packageBin) <+= (scalaVersion, sourceDirectory).map{(scalaVersion:String, srcDir:File) =>
	val manifest = new java.util.jar.Manifest(new java.io.FileInputStream(srcDir + "/main/MANIFEST.MF"))
	//
	manifest.getAttributes("scala/").putValue("Implementation-Version", scalaVersion)
	//
	Package.JarManifest( manifest )
}



javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked")

scalacOptions ++= Seq("-unchecked", "-deprecation" )

scalacOptions <++= scalaVersion.map{(sv:String) =>
	if (sv.take(3) == "2.1") {Seq("-feature", "-language:implicitConversions")} else {Nil}
}

excludeFilter in unmanagedSources in Compile := new FileFilter{
	def accept(n:File) = {
		val abPath = n.getAbsolutePath().replace('\\', '/')
		(
			(abPath endsWith "???")
		)
	}
}


// scalaTest
scalaVersion in Test := "2.11.2"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"

testOptions in Test += Tests.Argument("-oS")



proguardSettings

ProguardKeys.options in Proguard <+= (baseDirectory in Compile).map{"-include '"+_+"/sample.proguard'"}

ProguardKeys.inputFilter in Proguard := { file =>
	if (file.name.startsWith("gamescript")) {
		None
	} else if (file.name.startsWith("rt")) {
		Some("**.class;java.**;javax.**")
	} else {
		Some("**.class")
	}
}
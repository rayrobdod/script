addSbtPlugin("com.typesafe.sbt" % "sbt-proguard" % "0.2.2")

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases"

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")

// only works with scala 2.11
// addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "0.94.6")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.6.0")

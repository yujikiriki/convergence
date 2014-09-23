import sbt._
import sbt.Keys._

import bintray.Plugin._
import bintray.Keys._

object Build extends Build {

  val customBintraySettings = bintrayPublishSettings ++ Seq(
    packageLabels in bintray       := Seq("CRDT"),
    bintrayOrganization in bintray := Some("plasmaconduit"),
    repository in bintray          := "releases"
  )

  val root = Project("root", file("."))
    .settings(customBintraySettings: _*)
    .settings(
      name                := "convergence",
      organization        := "com.plasmaconduit",
      version             := "0.1.0",
      scalaVersion        := "2.11.2",
      licenses            += ("MIT", url("http://opensource.org/licenses/MIT")),
      libraryDependencies += "com.twitter" % "algebird-core_2.10" % "0.8.0"
    )

}
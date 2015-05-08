//
// Metadata
//

organization in ThisBuild := "dk.cwconsult"

version in ThisBuild := "1.0.0-SNAPSHOT"

//
// Compiler settings
//

scalaVersion in ThisBuild := "2.11.6"

scalacOptions in ThisBuild := Seq(
  "-Xlint", "-deprecation", "-unchecked", "-feature", "-encoding", "utf8"
)

//
// sbt-pgp settings
//

useGpg := true

// ==============================================================
// Projects
// ==============================================================

val postgreSQLFixtureJdbc = ProjectDef
  .inDirectory("postgresql-fixture-jdbc")

val postgreSQLFixtureScalikeJDBC = ProjectDef
  .inDirectory("postgresql-fixture-scalikejdbc")
  .dependsOn(
    postgreSQLFixtureJdbc
  )

// SparkSQL can work with a Spark built with Scala 2.11 too.

scalaVersion := "2.12.14"

version := "1.0.0"
val sparkVersion = "3.3.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "com.thesamet.scalapb" %% "sparksql33-scalapb0_11" % "1.0.2",
  //"com.thesamet.scalapb" %% "sparksql32-scalapb0_11" % "1.0.0"
)

libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion

// Hadoop contains an old protobuf runtime that is not binary compatible
// with 3.0.0.  We shaded ours to prevent runtime issues.
assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.google.protobuf.**" -> "shadeproto.@1").inAll,
  ShadeRule.rename("scala.collection.compat.**" -> "scalacompat.@1").inAll,
  ShadeRule.rename("shapeless.**" -> "shadeshapeless.@1").inAll
)

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)


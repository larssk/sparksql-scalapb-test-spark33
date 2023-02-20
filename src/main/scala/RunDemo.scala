package myexample

import com.example.protos.demo._

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SQLContext
import org.apache.spark.rdd.RDD
import scalapb.spark.Implicits._
import scalapb.spark.ProtoSQL

object RunDemo {

  def main(Args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("ScalaPB Demo").config("spark.master", "local").getOrCreate()

    val sc = spark.sparkContext
    val kafkaServers = "localhost:9092"
    val kafkaTopic = "persons"

    val produce = false

    if (produce) {
      val personsDS2: Dataset[Person] = spark.createDataset(testData)

      // convert to dataset of byte array
      val binaryDS: Dataset[Array[Byte]] = personsDS2
        .map(_.toByteArray)

      binaryDS.show()

      binaryDS
        .write
        .format("kafka")
        .option("kafka.bootstrap.servers", kafkaServers)
        .option("topic", kafkaTopic)
        .save()

    } else {

      val assetDf = spark
        .read
        .format("kafka")
        .option("kafka.bootstrap.servers", kafkaServers)
        .option("subscribe", kafkaTopic)
        .option("startingOffsets", "earliest")
        .load()
        .map(row => row.getAs[Array[Byte]]("value"))
        .map(Person.parseFrom(_))
        .toDF()

      assetDf.show()

    }
  }

  val testData: Seq[Person] = Seq(
    Person().update(
      _.name := "Joe",
      _.age := 32,
      _.gender := Gender.MALE),
    Person().update(
      _.name := "Mark",
      _.age := 21,
      _.gender := Gender.MALE,
      _.addresses := Seq(
        Address(city = Some("San Francisco"), street = Some("3rd Street"))
      )),
    Person().update(
      _.name := "Steven",
      _.gender := Gender.MALE,
      _.addresses := Seq(
        Address(city = Some("San Francisco"), street = Some("5th Street")),
        Address(city = Some("Sunnyvale"), street = Some("Wolfe"))
      )),
    Person().update(
      _.name := "Batya",
      _.age := 11,
      _.gender := Gender.FEMALE))
}

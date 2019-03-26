package test

import java.util.Properties

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.elasticsearch.spark.rdd.EsSpark

/**
  * Created by Administrator on 2018/11/5.
  */
object SparkGraphxTest {
  def main(args: Array[String]) {


  }

  def getSparkSession(): SparkSession = {
    val sparkConf = new SparkConf()
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.es.index.auto.create", getPropertiesValue("spark.es.index.auto.create"))
      .set("spark.es.nodes", getPropertiesValue("spark.es.nodes"))
      .set("spark.es.port", getPropertiesValue("spark.es.port"))
      .set("spark.es.net.http.auth.user", getPropertiesValue("spark.es.net.http.auth.user"))
      .set("spark.es.net.http.auth.pass", getPropertiesValue("spark.es.net.http.auth.pass"))
      .set("spark.es.resource", getPropertiesValue("spark.es.resource"))
      .set("spark.es.nodes.wan.only", getPropertiesValue("spark.es.nodes.wan.only"))
      .set("spark.executor.extraJavaOptions", "-XX:+UseConcMarkSweepGC")

    SparkSession.builder().appName(this.getClass.getSimpleName).master("local[*]")
      .config(sparkConf).getOrCreate()
  }

  def getPropertiesValue(key: String): String = {
    val properties = new Properties()
    val in = SparkGraphxTest.getClass.getClassLoader.getResourceAsStream("app.properties")
    properties.load(in)
    properties.getProperty(key)
  }
}

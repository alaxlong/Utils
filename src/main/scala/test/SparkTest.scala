package test

import org.apache.spark.sql.SparkSession

/**
  * Created by Administrator on 2018/10/17.
  */
object SparkTest {
  def main(args: Array[String]) {
    val spark = SparkSession
      .builder()
      .appName("spark test")
      .master("local")
      .getOrCreate()

    val sc = spark.sparkContext
    val rdd = sc.parallelize(1 to 100000)

    val data = rdd.sum()
    println(data + "**************")

    spark.stop()
  }

}

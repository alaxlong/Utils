package test

import scala.math.random
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2018/10/24.
  */
object SparkPi {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Spark Pi")/*.setMaster("spark://192.168.208.51:7077")*//*
      .setJars(List("D:\\IdeaProjects\\All-Class\\out\\artifacts\\SparkPi_jar2\\All-Class.jar"))*/
    val spark = new SparkContext(conf)
    val slices = if (args.length > 0) args(0).toInt else 2
    val n = 100000 * slices
    val count = spark.parallelize(1 to n, slices).map { i =>
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x * x + y * y < 1) 1 else 0
    }.reduce(_ + _)
    println("Pi is roughly " + 4.0 * count / n)
    spark.stop()
  }
}

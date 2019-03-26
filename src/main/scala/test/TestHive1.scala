package test

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.hive.HiveContext

/**
  * Created by Administrator on 2018/12/24.
  */
object TestHive1 {
  def main(args: Array[String]) {
    val spark = SparkSession
      .builder()
      .appName("Graphs")
      .master("local")
      .enableHiveSupport()
      .getOrCreate()

    val sc = spark.sparkContext
    val sqlContext = new HiveContext(sc)
    //    sqlContext.table("chinapex_etl.gf_uniq") // 库名.表名 的格式
    sqlContext.table("chunqiu.idmapping") // 库名.表名 的格式
      .registerTempTable("person")  // 注册成临时表

    val rdd = sqlContext.sql("select * from person").rdd
    println(rdd.count() + "~~~~~~~~~")
  }
}

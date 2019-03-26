import java.util.{Date, Properties}

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.CellUtil._
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableOutputFormat}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import util.HbaseUtil

import scala.reflect.ClassTag

/**
  * Created by Administrator on 2018/11/20.
  */
object HbaseTest {
  def main(args: Array[String]) {
    val date = new Date()

    println(date.getTime)
    println("abc".hashCode)
    println("abc".hashCode)
    val tableName = "mapping"
    val columnFamilys = Array("cf")
//    HbaseUtil.createTable(tableName, columnFamilys)

    val row = "aaa"
    val family = "cf"
    /*HbaseUtil.addRow(tableName, "row1", family, "src", "user1")
    HbaseUtil.addRow(tableName, "row1", family, "dist", "order1")
    HbaseUtil.addRow(tableName, "row2", family, "src", "order1")
    HbaseUtil.addRow(tableName, "row2", family, "dist", "ori1")
    HbaseUtil.addRow(tableName, "row3", family, "src", "ori1")
    HbaseUtil.addRow(tableName, "row3", family, "dist", "visitor1")
    HbaseUtil.addRow(tableName, "row4", family, "src", "ori1")
    HbaseUtil.addRow(tableName, "row4", family, "dist", "visitor2")


    HbaseUtil.addRow(tableName, "row5", family, "src", "user1")
    HbaseUtil.addRow(tableName, "row5", family, "dist", "order2")
    HbaseUtil.addRow(tableName, "row6", family, "src", "order2")
    HbaseUtil.addRow(tableName, "row6", family, "dist", "ori2")
    HbaseUtil.addRow(tableName, "row7", family, "src", "ori2")
    HbaseUtil.addRow(tableName, "row7", family, "dist", "visitor3")


    HbaseUtil.addRow(tableName, "row8", family, "src", "user2")
    HbaseUtil.addRow(tableName, "row8", family, "dist", "order3")


    HbaseUtil.addRow(tableName, "row9", family, "src", "order4")
    HbaseUtil.addRow(tableName, "row9", family, "dist", "ori3")
    HbaseUtil.addRow(tableName, "row0", family, "src", "ori4")
    HbaseUtil.addRow(tableName, "row0", family, "dist", "visitor4")*/
//    HbaseUtil.delRow(tableName, row)


    def loadData(tableName: String, startRow: String, stopRow: String, startTimeStamp: Long, endTimeStamp: Long, sparkContext: SparkContext): RDD[(String, Map[String, String])] = {
      getAllRawRDD(sparkContext, tableName, startRow, stopRow, startTimeStamp, endTimeStamp, res2StrMapWithK) //.coalesce(32, false)
    }

    def res2StrMap(res: Result): Map[String, String] = {
      val cells = res.rawCells()
      cells.map(cell => {
        (Bytes.toString(cloneQualifier(cell)), Bytes.toString(cloneValue(cell)))
      }).toMap
    }

    def res2StrMapWithK(res: Result): (String, Map[String, String]) = {
      (Bytes.toString(res.getRow), res2StrMap(res))
    }

    def getAllRawRDD[T: ClassTag](
                                   _sparkContext: SparkContext,
                                   tableName: String,
                                   startRow: String,
                                   stopRow: String,
                                   startTimeStamp: Long,
                                   endTimeStamp: Long,
                                   toT: Result => T
                                 ): RDD[T] = {
      val hConf = getHBaseConf(tableName, true)
      hConf.set(TableInputFormat.SCAN_ROW_START, startRow)
      hConf.set(TableInputFormat.SCAN_ROW_STOP, stopRow)
      hConf.set(TableInputFormat.SCAN_TIMERANGE_START, startTimeStamp.toString)
      hConf.set(TableInputFormat.SCAN_TIMERANGE_END, endTimeStamp.toString)

      val rawRDD: RDD[(ImmutableBytesWritable, Result)] = _sparkContext.newAPIHadoopRDD(hConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
      rawRDD.map(res => toT(res._2))
    }

    def getHBaseConf(tableName: String, isInput: Boolean): Configuration = {
      //    println("***HBaseConfiguration.create")
      val hConf = HBaseConfiguration.create()

      //chunqiu
      hConf.set("hbase.zookeeper.quorum", getPropertiesValue("hbase.zookeeper.quorum"))
      //    hConf.set("hbase.zookeeper.quorum", "master20851")
      //    hConf.set("hbase.zookeeper.quorum", "master")
      hConf.set("hbase.zookeeper.property.clientPort", getPropertiesValue("hbase.zookeeper.property.clientPort"))

      hConf.set("zookeeper.znode.parent", getPropertiesValue("zookeeper.znode.parent"))
//      hConf.set("mapreduce.output.fileoutputformat.outputdir", getPropertiesValue("mapreduce.output.fileoutputformat.outputdir"))
      if (isInput)
        hConf.set(TableInputFormat.INPUT_TABLE, tableName)
      else
        hConf.set(TableOutputFormat.OUTPUT_TABLE, tableName)
      hConf
    }

    def getPropertiesValue(key: String): String = {
      val properties = new Properties()
      val in = HbaseTest.getClass.getClassLoader.getResourceAsStream("app.properties")
      //val path = Thread.currentThread().getContextClassLoader.getResource("config_scala.properties").getPath //文件要放到resource文件夹下
      properties.load(in)
      //    println(properties.getProperty("spring.datasource.url"))
      properties.getProperty(key)
    }

  }

}

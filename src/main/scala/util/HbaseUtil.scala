package util

import java.{lang, util}

import com.mongodb.casbah.commons.MongoDBObject
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase._
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp
import org.apache.hadoop.hbase.filter._

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

/**
  * Created by baibing.shang on 2017/7/14.
  */

object HbaseUtil {
  private val conf: Configuration = ConfigUtil.apply.createHbaseConfig
//  private val conf = HBaseConfiguration.create()
//  conf.set("hbase.zookeeper.quorum", "10.10.10.127,10.10.10.128,10.10.10.129")
//  conf.set("hbase.zookeeper.property.clientPort", "2181")
//  conf.addResource("hbase-site.xml")

  /**
    * 查询表是否存在
    *
    * @param tableName
    */
  def isExist(tableName: String): Boolean = {
    val hAdmin: HBaseAdmin = new HBaseAdmin(conf)
    hAdmin.tableExists(tableName)
  }

  /**
    * 创建表
    *
    * @param tableName
    * @param columnFamilys
    */
  def createTable(tableName: String, columnFamilys: Array[String]): Unit = {
    val hAdmin: HBaseAdmin = new HBaseAdmin(conf)
    if (hAdmin.tableExists(tableName)) {
      println("表" + tableName + "已经存在")
      return
    } else {
      val tableDesc: HTableDescriptor = new HTableDescriptor(tableName)
      for (columnFaily <- columnFamilys) {
        tableDesc.addFamily(new HColumnDescriptor(columnFaily))
      }
      hAdmin.createTable(tableDesc)
      println("创建表成功")
    }
  }

  /**
    * 删除表
    *
    * @param tableName
    */
  def deleteTable(tableName: String): Unit = {
    val admin: HBaseAdmin = new HBaseAdmin(conf)
    if (admin.tableExists(tableName)) {
      admin.disableTable(tableName)
      admin.deleteTable(tableName)
      println("删除表成功!")
    } else {
      println("表" + tableName + " 不存在")
    }
  }

  /**
    * 添加一条数据
    *
    * @param tableName
    * @param row
    * @param columnFaily
    * @param column
    * @param value
    */
  def addRow(tableName: String, row: String, columnFaily: String, column: String, value: String): Unit = {
    val table: HTable = new HTable(conf, tableName)
    val put: Put = new Put(Bytes.toBytes(row))
    put.add(Bytes.toBytes(columnFaily), Bytes.toBytes(column), Bytes.toBytes(value))
    table.put(put)
  }

  /**
    * 删除一条数据
    *
    * @param tableName
    * @param row
    */
  def delRow(tableName: String, row: String): Unit = {
    val table: HTable = new HTable(conf, tableName)
    val delete: Delete = new Delete(Bytes.toBytes(row))
    table.delete(delete)
  }

  /**
    * 删除多条数据
    *
    * @param tableName
    * @param rows
    */
  def delMultiRows(tableName: String, rows: Array[String]): Unit = {
    val table: HTable = new HTable(conf, tableName)
    val deleteList = for (row <- rows) yield new Delete(Bytes.toBytes(row))
    table.delete(deleteList.toSeq.asJava)
  }

  /**
    * 通过rowkey查询一条数据
    *
    * @param tableName
    * @param row
    */
  def getRow(tableName: String, row: String): Result = {
    val table: HTable = new HTable(conf, tableName)
    val get: Get = new Get(Bytes.toBytes(row))
    val result: Result = table.get(get)
    for (rowKv <- result.raw()) {
      println(new String(rowKv.getFamily))
      println(new String(rowKv.getQualifier))
      println(rowKv.getTimestamp)
      println(new String(rowKv.getRow))
      println(new String(rowKv.getValue))
    }
    result
  }

  /**
    * 获取所有数据
    *
    * @param tableName
    */
  def getAllRows(tableName: String): util.Iterator[Result] = {
    val table: HTable = new HTable(conf, tableName)
    val scan = new Scan()
//    scan.setBatch(1)
//    scan.setCacheBlocks(true)
    scan.setCaching(5000)
//    scan.setMaxVersions(1)
//    scan.setStartRow(Bytes.toBytes("8449_e4f0b228f78cde83befc8251d935cb1d"))
    //af53_FFE95448-8604-4E58-BE92-0117177F9FDC   2017-11-20
//    scan.setStartRow(Bytes.toBytes("1d91_818d258e68c56b3e6baeb38f3c222fa4"))

//    e631_9d59adc0881ff9d67d8f59a0d6fce6ef

    scan.setStartRow(Bytes.toBytes("16de_9F393D1E-C0F6-464C-B460-A095F7696F37"))
//
//    scan.setStopRow(Bytes.toBytes("1510_2C3F6EAB3141F5CCD12C0D9C1DCCD693"))

//    scan.setStopRow(Bytes.toBytes("af53_G9eGc0ETScL6"))
//    scan.setStopRow(Bytes.toBytes("9999_"))
//    scan.setRowPrefixFilter(Bytes.toBytes("0000_9"))

//    scan.setMaxVersions(1)
//    val results: ResultScanner = table.getScanner(new Scan())
//    scan.setStartRow(Bytes.toBytes("faff_a"))
    //倒叙
//    scan.setReversed(true)
    //过滤器
//    val rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("tag")))
    val qualifier = new QualifierFilter(CompareOp.NOT_EQUAL, new BinaryComparator(Bytes.toBytes("p")))
    scan.setFilter(qualifier)

    val results: ResultScanner = table.getScanner(scan)
    val it: util.Iterator[Result] = results.iterator()
    it
  }


  def main(args: Array[String]) {
    //TestHbaeJavaApi.createTable("testApi",Array("info","two"))
    //TestHbaeJavaApi.addRow("testApi","row2","info","get","getTwo")
    //TestHbaeJavaApi.delRow("testApi","row2")
//    HbaseUtil.this.getRow("dxp:mz_tag","1510_0f13404ec64077a8f9aa8fe7161a20e0")
//    HbaseUtil.this.getRow("test","1")
//    val it = HbaseUtil.getAllRows("dxp:mz_tag")

    val it = HbaseUtil.getAllRows("dxp:mz_tag")
    var sum = 0
    println(new String(it.next().getRow))
    val startUpdate = System.currentTimeMillis()
    while (it.hasNext) {
      sum += 1
      println("Hbase count:" + sum)
      if (sum == 1000000){
        println("数据完成，共耗时："+(System.currentTimeMillis() - startUpdate)+"毫秒")
        //10W 数据完成，共耗时：6779毫秒
        //100W 数据完成，共耗时：69265毫秒
        //10W spark 数据完成，共耗时：38076毫秒
        //100W spark 数据完成，共耗时：221517毫秒
        System.exit(1)
      }
      val next: Result = it.next()
//      println("*************")
      for (kv <- next.raw()) {
        val str = new String(kv.getRow)
        println("Row: " + new String(kv.getRow))
        println("Family: " + new String(kv.getFamily))
        println("Qualifier: " + new String(kv.getQualifier))
        println(new String(kv.getValue))
//        println(kv.getTimestamp)
        println("---------------------------------")
      }
    }

    //------------------------------------------------
    /*val table: HTable = new HTable(conf, "dxp:mz_tag")
    val scan = new Scan()
    scan.setBatch(1)
    scan.setCaching(3000)
    val results: ResultScanner = table.getScanner(scan)*/

    /*val it = results.iterator()
    while (it.hasNext){
      val next = it.next()
      println("******************")
    for (i <- next.raw()) {
        println(new String(i.getRow))
        println(new String(i.getFamily))
        println(new String(i.getQualifier))
        println(new String(i.getValue))
        /*val row = new String(i.getRow)
        HbaseUtil.getRow("dxp:mz_tag", row)*/
      println("--------------------------------------")
      }
    }*/



  }
}

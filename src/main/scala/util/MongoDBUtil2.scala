package util

import java.io.{File, PrintWriter}
import java.util
import java.util.Properties

import com.mongodb.casbah.MongoClientOptions
import com.mongodb.client.model._
import com.mongodb.client.{MongoCollection, MongoDatabase}
import com.mongodb.{BasicDBList, MongoClient, MongoCredential, ServerAddress}
import org.bson
import org.bson.Document
import org.bson.conversions.Bson

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

/**
  * mongodb read and write
  * Created by shangbaibing on 2017/7/13
  */
final object MongoDBUtil2 {
  var mongoClient: MongoClient = _
  var db:MongoDatabase = _
  var coll: MongoCollection[Document] = _
  var insertTable:String = _
  val prop = new Properties()
  init()


  private def init() {
    val file = getClass.getResourceAsStream("mongodb.properties")
    prop.load(file)

    val hosts = prop.get("hosts").toString
    val port = prop.get("port").toString.toInt
    val dbName = prop.get("dbName").toString
    val tbName = prop.get("tbName").toString
    val user = prop.get("user").toString
    val password = prop.get("password").toString

    //使用密码登陆
    this.mongoClient = getMongoClientWithPassWord(hosts, port, dbName, user, password)
    //无密码登陆
//    this.mongoClient = getMongoClient(hosts, port)
    this.db = this.mongoClient.getDatabase(dbName)
    this.coll = this.db.getCollection(tbName)
    this.insertTable = tbName
    println("init MongoUtil")
  }

  /**
    * 无密码登陆
    *
    * @param hosts
    * @param port
    * @return
    */
  private def getMongoClient(hosts: String, port: Int): MongoClient = {
    val sb = ArrayBuffer[ServerAddress]()
    val hostArr = hosts.split(",")
    for (host <- hostArr) {
      val addr = new ServerAddress(host, port)
      sb += addr
    }

    new MongoClient(sb)
  }

  /**
    * 有密码登陆
    * @param hosts
    * @param port
    * @param dbName
    * @param user
    * @param pwd
    * @return
    */
  private def getMongoClientWithPassWord(hosts: String, port: Int, dbName: String, user: String, pwd: String): MongoClient = {
    val sb = ArrayBuffer[ServerAddress]()
    val hostArr = hosts.split(",")
    for (host <- hostArr) {
      val addr = new ServerAddress(host, port)
      sb += addr
    }

    val sb2 = ArrayBuffer[MongoCredential]()
    val crc = MongoCredential.createCredential(user, dbName, pwd.toCharArray())
    sb2 += crc

    val build: MongoClientOptions.Builder = new MongoClientOptions.Builder
    build.connectionsPerHost(400)
    build.threadsAllowedToBlockForConnectionMultiplier(10)
    build.cursorFinalizerEnabled(true)

    new MongoClient(sb, sb2, build.build())

//    new MongoClient(sb, sb2)
  }

  /**
    * 批量插入
    * @param docs
    */
  def bulkWriteInsert(docs: util.ArrayList[Document]): Unit ={
    val conn: MongoCollection[Document] = db.getCollection( insertTable )
    val requests: util.ArrayList[WriteModel[Document]] = new util.ArrayList[WriteModel[Document]]()
    for (i <- 0 until docs.size()){
      val iom = new InsertOneModel[Document](docs.get(i))
      requests.add(iom)
      //      println(doc)
    }
    val blukWriteResult = conn.bulkWrite(requests, new BulkWriteOptions().ordered(false))
  }

  /**
    * 批量更新
    */
  def bulkWriteUpdate(docs: util.ArrayList[Document]): Unit ={
    val conn: MongoCollection[Document] = db.getCollection( insertTable )
    val requests: util.ArrayList[WriteModel[Document]] = new util.ArrayList[WriteModel[Document]]()
    for (i <- 0 until docs.size()) {
      //更新条件
      val queryDocument = new Document("_id", docs.get(i).getString("_id")).append("hash_id", docs.get(i).getInteger("hash_id").toInt)
      //更新内容
      val updateDocument = new Document("$set", docs.get(i))
      //      println(updateDocument)
      //构造更新单个文档的操作模型
      val uom = new UpdateOneModel[Document](queryDocument,updateDocument,new UpdateOptions().upsert(true))
      //UpdateOptions代表批量更新操作未匹配到查询条件时的动作，默认false，什么都不干，true时表示将一个新的Document插入数据库，他是查询部分和更新部分的结合
      requests.add(uom)
    }
    val bulkWriteResult = conn.bulkWrite(requests,  new BulkWriteOptions().ordered(false))
//    println(bulkWriteResult.toString())
  }


  def bulkWriteDelete(docs: util.ArrayList[Document]): Unit = {
    val conn: MongoCollection[Document] = db.getCollection( insertTable )
    val requests = new util.ArrayList[WriteModel[Document]]()
    for (i <- 0 until docs.size()) {
      //删除条件
      val queryDocument = new Document("_id",docs.get(i).getString("_id"))
      //构造删除单个文档的操作模型，
      val  dom = new DeleteOneModel[Document](queryDocument)
      requests.add(dom)
    }
    val bulkWriteResult = conn.bulkWrite(requests)
    System.out.println(bulkWriteResult.toString())
  }


  /**
    * InsertMany批量插入
    *
    * @param listArr
    */

  def insertMore(listArr: util.ArrayList[Document] ): Unit ={

    val doc: MongoCollection[Document] = db.getCollection( insertTable )
    doc.insertMany(listArr)
  }

  /**
    * update elements
    *
    * @param condition eg:Filters.eq( "age","28" ) == "age" == "28"
    * @param document
    */
  def updateElement( condition:Bson , document:Document ): Unit ={
    try{
      val doc: MongoCollection[Document] = db.getCollection( insertTable )
      doc.updateMany(
        condition,
        new Document( "$set" , document),
        //未查到要更新的记录，true则新增
        new UpdateOptions().upsert(false)
      )
    }catch{
      case e:Exception => throw new IllegalArgumentException(s"update mongodb the fail ${e}")
    }
  }

  /**
    * 更新数据类型
    *
    * @param condition
    * @param document
    */
  def updateSetElement( condition: Document , document:Document ): Unit ={
    try{
      val doc: MongoCollection[Document] = db.getCollection( insertTable )
      doc.updateMany(
        condition,
        new Document( "$set" , document),
        //未查到要更新的记录，true则新增
        new UpdateOptions().upsert(false)
      )
    }catch{
      case e:Exception => throw new IllegalArgumentException(s"update mongodb the fail ${e}")
    }
  }

  /**
    * 通过clientid查询一条数据
    * （clientid是没有MD5加密前的）
    *
    * @param clientid
    * @return 返回查询的一条数据
    */
  def findElementMD5 (clientid: String) = {
    try{
      val doc: MongoCollection[Document] = db.getCollection( insertTable )
      val document = new bson.Document().append("clientid", CommUtil.md5(clientid).toLowerCase)
      println(document.toJson())
      doc.find(document).head
    }catch{
      case e:Exception => throw new IllegalArgumentException(s"find mongodb the fail ${e}")
    }
  }

  /**
    * 统计一个tag_id的条数
    * @param tagid
    * @return   返回Int类型条数
    */
  def countOneElement (tagid: String) = {
    try{
      val doc: MongoCollection[Document] = db.getCollection( insertTable )
      val document = new bson.Document().append("third_crowd_tag.miaozhen.demographic.tag_id", tagid)
      println(document.toJson())
      doc.count(document)
    }catch{
      case e:Exception => throw new IllegalArgumentException(s"find mongodb the fail ${e}")
    }
  }

  /**
    * 通过传入一个由tag_id组成的字符串，来统计相关的条数
    * 参数格式：300010002200020003|300010002200020004,300010002200010001
    * A|B,D   A|B|C,D ....
    * @param str
    * @return
    */
  def countTagElement (str: String): Long = {
    try{
      val doc: MongoCollection[Document] = db.getCollection( insertTable )
      val document = new Document()
      val tagList = new BasicDBList()
      if (str.contains(",")){
        val strArr = str.split(",")
        val arr: Array[String] = strArr(0).split("\\|")
        for (i <- 0 until arr.length){
          tagList.add(arr(i))
        }
        document.append("third_crowd_tag.miaozhen.demographic",new Document("$elemMatch", new Document("tag_id", new Document("$in", tagList))))

        val tagList2 = new BasicDBList()
        val arr2: Array[String] = strArr(1).split("\\|")
        for (i <- 0 until arr2.length){
          tagList2.add(arr2(i))
        }

        document.append("third_crowd_tag.miaozhen.demographic.tag_id", new Document("$in", tagList2))
      } else {
        val arr: Array[String] = str.split("\\|")
        for (i <- 0 until arr.length){
          tagList.add(arr(i))
        }
        document.append("third_crowd_tag.miaozhen.demographic",new Document("$elemMatch", new Document("tag_id", new Document("$in", tagList))))
      }
      println(document.toJson)
      doc.count(document)
    }catch{
      case e:Exception => throw new IllegalArgumentException(s"find mongodb the fail ${e}")
    }
  }


  def main(args: Array[String]): Unit = {
//    println(findElement("00047C24-51C8-4CB3-AEFA-C14F451B257B"))
//    println(countOneElement("300010002200010001"))

//    println(countTagElement("300010002200020003|300010002200020004|300010002200020005,300010002200010001"))
//    println(countTagElement("300010002200020003|300010002200020004"))
//    println(countTagElement("300010002200020003|300010002200020004,300010002200020005|300010002200010001"))
    /*try{
//            println("统计条数为：" + countTagElement(args(0)))
    } catch {
      case e: Exception => println("请输入正确的参数格式： java -jar Hdt.jar \"tag_id|tag_id,tag_id\"")
    }*/

    val writer = new PrintWriter(new File("E:\\mongo3.txt"))
    val doc: MongoCollection[Document] = db.getCollection( insertTable )
    val document = new bson.Document().append("device.cookieid", new Document("$exists", false))
    document.append("third_crowd_tag.miaozhen.demographic", new Document("$size", 2))
    println(document.toJson())
    val it = doc.find(document).limit(25000).toIterator

    while (it.hasNext){
      /*println(it.next().get("clientid") + " " + it.next().get("third_crowd_tag"))
      writer.println(it.next().get("clientid") + " " + it.next().get("third_crowd_tag"))*/
      val res = it.next()
//      println(res)
      val arr = res.get("third_crowd_tag").asInstanceOf[Document].get("miaozhen").asInstanceOf[Document].get("demographic").asInstanceOf[util.ArrayList[Document]]
//      println(arr)
      var buf = new  ArrayBuffer[String]()
      for (i <- arr){
        buf.append(i.get("name").toString)
      }
//      println(res.get("clientid") + "," + buf.toString())
      writer.println(res.get("clientid") + "," + buf.toString())
    }
    writer.flush()
    writer.close()

  }

}





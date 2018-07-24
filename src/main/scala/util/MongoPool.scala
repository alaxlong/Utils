package util

import com.mongodb.{MongoClient, ServerAddress}

/**
  * Created by baibing.shang on 2018/4/26.
  */
object MongoPool {
  var  instances = Map[String, MongoClient]()

  //node1:port1,node2:port2 -> node
  def nodes2ServerList(nodes : String):java.util.List[ServerAddress] = {
    val serverList = new java.util.ArrayList[ServerAddress]()
    nodes.split(",")
      .map(portNode => portNode.split(":"))
      .flatMap{ar =>{
        if (ar.length==2){
          Some(ar(0),ar(1).toInt)
        }else{
          None
        }
      }}
      .foreach{case (node,port) => serverList.add(new ServerAddress(node, port))}

    serverList
  }

  def apply(nodes : String) : MongoClient = {
    instances.getOrElse(nodes,{
      val servers = nodes2ServerList(nodes)
      val client =  new MongoClient(servers)
      instances += nodes -> client
      println("new client added")
      client
    })
  }

  def main(args: Array[String]) {
    val client = apply("172.16.2.187:27017")
    val conn = client.getDatabase("test").getCollection("t0")
    println(conn.count())
  }
}

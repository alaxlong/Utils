package util

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory}
/**
  * Created by baibing.shang on 2017/7/14.
  */
class ConfigUtil private {
  private var connection:Connection = _

  def createHbaseConfig = {
    val conf: Configuration = HBaseConfiguration.create()
//    conf.set("hbase.zookeeper.quorum", "139.219.5.86")
    conf.set("hbase.zookeeper.quorum", "42.159.86.11") //hive1
//    conf.set("hbase.zookeeper.quorum", "10.0.3.6")
//    conf.set("hbase.zookeeper.quorum", "192.168.208.51")
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("zookeeper.znode.parent", "/hbase")
//    conf.addResource("hbase-site.xml")
    conf
  }

  def createHadoopConfig ={
    val conf: Configuration = new Configuration()
    //    conf.addResource("core-site.xml")
    //    conf.addResource("hdfs-site.xml")
//    conf.addResource("hbase-site.xml")
    conf
  }


  def build(zooKeeperQuorum: String = "192.168.1.230",
            clientPort: Int = 2181,
            znodeParent: String = "/hbase") = {

    val conf = new Configuration()
    conf.set("hbase.zookeeper.quorum", zooKeeperQuorum)
    conf.set("hbase.zookeeper.property.clientPort", clientPort.toString)
    conf.set("zookeeper.znode.parent", znodeParent)
    val hBaseConf = HBaseConfiguration.create(conf)

    if(connection != null) throw new Exception("connection already init")
    connection = ConnectionFactory.createConnection(hBaseConf)
    //    if (pool == null) {
    //      this.hconf = hBaseConf
    //      val config = new GenericObjectPoolConfig()
    //      config.setMaxTotal(20)
    //      config.setMinIdle(3)
    //      config.setMaxIdle(5)
    //      pool = new GenericObjectPool[Connection](
    //        new HbaseConnectionFactory(hBaseConf),
    //        config)
    //    } else {
    //      throw new Exception("pool arealdy built")
    //    }
    this
  }


}

object ConfigUtil{
  def apply: ConfigUtil = new ConfigUtil()

}

package util

import com.typesafe.config.{Config, ConfigFactory, ConfigRenderOptions}
import java.io.{File, FileInputStream}
import java.util.Properties

import org.slf4j.LoggerFactory
import java.net.InetAddress

//import com.chinapex.dao.PrismAddressDao
/**
  * Created by ning on 16-11-1.
  */
object ConfigurationHelper {
  val logger = LoggerFactory.getLogger(this.getClass)

  var applicationPort = 1947
//  val netAddress: String = InetAddress.getLocalHost.getHostName
  var zohoCrmAddress = "http://192.168.1.230:8889"
  var hbaseZookeeperIp = "192.168.3.2"
  var esAddr = ""
  var hbaseZookeeperPort = 2181
  var hbaseZnodeParent = "/hbase"

  var prismZookeeperIp = "10.0.0.44"
  var prismZookeeperPort = 2181
  var prismZnode = "/hbase"
  var prismTopic = "prism"
  /**
    * define Labels table (table name : Label , LabelManager)
    * */
  var allLabelTable = "Label"
  var allLabelTableFamily = "cf"

  var labelManagerTable = "LabelManager"
  var labelManagerFamily = "cf"

  var moduleTable = "ZohoModules"
  var moduleTableFamily = "f1"

  var baiduTongjiTable = "BaiduTongjiMetrics"
  var baiduTongjiFamily = "cf"

  var statusTable = "ZohoStatus"
  var statusFamily = "FL"
  var statusTableFamilyNo = "no"

  var zohoIndex = "ZohoIndex"
  var zohoIndexFamily = "cf"

  var cookieMappingTable = "CookieMapping"
  var cookieMappingFamily = "cf"

  var prismRealtimeDataTable = "prism_realtime_data_test"
  var prismFamily = "cf"

  var prismDataTable = "realtime_data"
  var prismDataFamily = "cf"

  var prismCursorTable = "realtime_cursor"
  var prismCursorFamily = "cf"

  var fakePrismRealtimeDataTable = "fake_prism_realtime_data"
  var fakePrismRealtimeDataFamily = "cf"

  var phoenixZnode = "/hbase-unsecure"
  var kafkaZooKeeperIp = "192.168.3.2"
  var kafkaZooKeeperPort = 2181
  var kafkaIp = "192.168.3.2"
  var kafkaPort = 9092

  var tempUserToken = "507f191e810c19729de860ea"

  var defaultOrganization = "chinapex"

  var isTest = false

  var isSyncPrism = true
  var isSyncZoho = true
  var isSyncBaidu = true
  var isAlterTable = false

  var sparkDataServiceUrl = ""
  var isSparkDataServiceTrigger = false

  val funnelCacheTableName = "funnel_cache"
  val funnelCacheColumnFamily = "cf"

  val ihuyiCallbackTable = "IhuyiCallback"
  val ihuyiReceiptCF = "rpt"
  val ihuyiReplyCF = "rly"

  val trackingRecordTable = "trackingRecord"
  val trackingRecordCF = "cf"


  /**系统版本**/
  var OS_VERSION = "Linux"

  /**客户端载体**/
  var DEVICE_TYPE = "Linux"

  /**客户端版本**/
  var CLIENT_VERSION = "4.4.5"


  /**登录地址**/
  var LOGIN_URL = "https://api.baidu.com/sem/common/HolmesLoginService"

  /**siteId地址**/
  //public static final String PROFILE_URL = "https://api.baidu.com/json/tongji/v1/ProductService/api";//(地址已过期！不要踩坑)
  var PROFILE_URL = "https://api.baidu.com/json/tongji/v1/ReportService/getSiteList"

  /**数据地址**/
  var DATA_URL = "https://api.baidu.com/json/tongji/v1/ReportService/getData"

  /**RSA公钥（文档可以获取到）**/
  var PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDHn/hfvTLRXV" +
    "iBXTmBhNYEIJeGGGDkmrYBxCRelriLEYEcrw"+
    "Wrzp0au9nEISpjMlXeEW4+T82bCM22+JUXZpIga5qdBrPkjU08Ktf5n7Nsd7n9ZeI0YoAKCub3ulVExcxGeS3RV"+
    "xFai9ozERlavpoTOdUzEH6YWHP4reFfpMpLzwIDAQAB"

  var excelTableFamily = "cf"
  var excelOutputFileDir = "/home/chinapex/frank/iq/data"
  var sparkUser = "chinapex"
  var sparkHdfsPath = "hdfs://192.168.3.5:9000/iqnode/"
  var excelOutputHdfsFileDir = "/iqnode"

  var fileUploadDir = "/file_upload"

  // salesforce
  var sfTableFamily = "cf"
  var sfTest = false
  var msSFDailySyncON = true
  var msSFDailySyncOrg = "penny"
  var msSFDailyClientID = "3MVG9Y6d_Btp4xp7oyJw27xIVRLywJiCrmGH2zz2k9.zlkAgsPTHRknAq7QwOfQlOEOWxvoQ5SyhoP_PQIZOw"
  var msSFDailyClientSecret = "1829944655512610073"
  var msSFDailyUserAccount = "DataSynchronism@maserati.com"
  var msSFDailyUserPwd = "maserati2017"

  // 不依赖于 master table 的标签会在数据源对应的 HBase table 中的 "lb" column family 留下标记
  var newLabelCF = "lb"


  var prismCustomerDataTable = "prism_customer_data"
  var prismCustomerIdFamily = "customer_ids"
  var prismCustomerInfoFamily = "customer_info"

  var prismEventDataTable = "prism_event_data"
  var prismEventInfoFamily = "event_info"
  var prismEventClientInfoFamily = "client_info"

  var prismCustomerEventDataTable = "prism_customer_event_data"
  var prismCustomerEventDataTableFamily_1 = "cf"
  var prismCustomerEventDataTableFamily_2 = "cf2"

  var syncDataFromPrismInStartSystem = false


  // mysql db address use this format: host:port
  // and mysql account & password
  var mysqlAddress = "jdbc:mysql://192.168.1.230/CDP_NING?useSSL=false"
  var mysqlAccount = "root"
  var mysqlPassword = ""

  //  var lableTable = "Zoholables"
  var prismAddress = "http://192.168.1.230:8001"
//  private val oldOrgId = Set("41a0ec77-c3ee-46a0-b474-e02ee0697dfa","7d5b6049-17ba-4d23-93f9-16a45f6fcf37")
//  private val oldOrgName = Set("banyantree","igola")
//  def getPrismAddressByOrgId(orgId:String):String = PrismAddressDao.findByOrgId(orgId).getOrElse(prismAddress)
//  def getPrismAddressByOrgName(orgName:String):String = PrismAddressDao.findByOrgName(orgName).getOrElse(prismAddress)

  var isMapCount = false

  var hiveConnectionPath = "jdbc:hive2://192.168.3.2:10000/default"
  var nexusSqlHbaseFamily = "cf"

  var labelRemoteCalActorAddr = "akka.tcp://data_service@192.168.1.199:2046/user/labelCalActor"
  var newLabelRemoteCalActorAddr = "akka.tcp://data_service@192.168.1.199:2046/user/newLabelCalActor"
  var labelCustomerPotraitRemoteActorAddr = "akka.tcp://data_service@192.168.1.199:2046/user/rpcServer"

  var audienceRemoteCalActorAddr = "akka.tcp://data_service@192.168.1.199:2046/user/audCalActor"

  var mtConnectRemoteActor = "akka.tcp://data_service@192.168.1.199:2046/user/mtConnectActor"

  val masterTableName = "masterTable"
  val masterTableColumnFamily = "cf"
  val masterCustomerDataId = "customerId"
  val masterTableCustomerRowKeyPrefix = "customer_"
  val masterTableNonCustomerRowKeyPrefix = "nonCustomer_"

  // 新的 master table
  val newMasterTable = "masterTablePro"
  val newMasterTableCF1 = "cf1" // 用作 id 性质的列
  val newMasterTableCF2 = "cf2"

  val newMasterTableCF = "cf"

  // 存储新的标签的 graph data
  val newLabelGraphTable = "LabelGraph"
  val newLabelGraphTableCF = "cf"
  val newLabelGraphTableDelimitChar = "@"

  val pageIndexTable = "pageIndexTableNew"
  val pageIndexTableFamily = "pageFamily"

  var ip2GeoTable = "ip_to_geo"
  var ip2GeoTableFamily = "geo"
  var iqModulesURI = "http://192.168.1.230:8050/iq/api/v1/iq/models"

  var localIpData = "ip.data"
  var getGeoLocal = false

  var rtbAsiaToken = "3562ea65b0fe82e39ab9a8233d741ea1"

  val iHuyiResult = "ihuyiResult"
  val iHuyiColumnFamily = "cf"

  val applicablePopulationTable = "applicablePopulation"
  val applicablePopulationColumnFamily = "cf"

  // 是否允许标签使用此前的coverScope 计算结果
  var labelUseCachedResult = true
  // label calculateCoverScope 计算频率(hours/time)
  var labelCoverScopeCalculateFrequency = 1
  // service launch 之后load label entity 的时候是否需要计算cover scope
  var labelCoverScopeActorStartHour = 2
  // time of day to start labelCoverScopeActor calculation
  var labelTriggerCalculatingWhenLaunch = true
  // 标签可视化计算报警时间
  var labelCalculateGraphDataTime = 1500
  // 标签计算actor数量
  var labelCoverScopeCalculateActorNumber = 1
  // Frank IQ 模块生成智能标签结果的DB name 和Table name
  var labelIQResultDB = "CDP"
  var labelIQResultDBTable = "KMeansPCAEntity"

  var syncPrismCustomerEventData = false

  var customerJourneyResultTable = "CustomerJourneyResult"

  var customerJourneyResultFamily = "cf"

  var ihuyiAccount = "M76890642"
  var ihuyiPassword = "305f45fd2d7723d048ff2c87fd6c38f7"

  var rootPath = ""

  var funnelCustomerFamily = "cf"

  var isFakeOverView: Boolean = false


  var funnelPreCalculatedResultTable = "PreCalcResult"

  var preCalcResultColumnFamily = "pcrcf"

  var funnelIntermediateResultFamily = "firfcf"

  var remoteHolderAddr = "akka.tcp://data_service@192.168.1.199:2046/user/holder"
  var remoteCalculationHolderAddr = "akka.tcp://data_service@192.168.1.199:2047/user/holder"

  var awsAccessId = "AKIAOHNO4OELJM2JFCUA"
  var awsAccessKey = "NsKstxI216S3pfvpwfzC39rrPtIshj4O6otUFnBb"

  var shouldInitHiveTable = false

  var syncCustomerdata = false

  var wechatToken = "w4059huw402456hwq354gq34"
  var wechatKey = "w40985uyjh0w9435uiypq03i5q345809jtg0qoi345u"
  var wechatAppId = "wx0670ba76fbebaf1e"
  var wechatAppSecret = "34a7c39dfe06a72584d7b00c59afbe1e"

  var aliCloudAccessKeyId = ""
  var aliCloudAccessKeySecret = ""
  var aliCloudSMSTable = "aliCloudSMS"
  var aliCloudSMSColumnFamily = "cf"
  var aliCloudSMSReceiptQueueName = ""
  var aliCloudSMSReplyQueueName = ""

  // 用于存储标签对应customer id 数据的HBase table, 代替此前的MySQL "LabelCustomerID" table
  var labelCustomerIDHTable = "labelCustomerIDHTable"
  var labelCustomerIDHTableCF = "cf"
  var labelCustomerIDHTableCidColumnQualifier = "cid"   // column name used to store customer id
  var labelCustomerIDHTableMTRKColumnQualifier = "mtrk" // column name used to store master table row key
  var labelCustomerIDHTableDelimitChar = "@"  // "labelCustomerIDHTable" 表中 rowkey 中的分隔符
  var labelCustomerIDHTableDelimitCharEnd = ":" // 用于构造 stopRow, 因为ASCII 编码中 1~9 下一个字符为 ":"

  // customer ID 和「标签」的对应关系
  var cidLabelHTable = "cidLabel"
  var cidLabelHTableCF = "cf"

  var labelCalculatingSwitchToSparkSql = false
  var prepareForLabelSparkSQL = false
  var convertExcelPhoenixEncoding = false
  var convertExcelPhoenixEncodingDone = false
  var closeLabelRecalculating = true
  var closeAudienceRecalculating = true
  var audienceCoverScopeActorStartHour = 4    // 人群重复计算凌晨时间


//  var emailServerHost = "todo"
//  var emailServerUserName = "todo"
//  var emailServerUserPassword = "todo"
//  var emailSendAddress= "todo"

//  var emailServerHost="smtpdm.aliyun.com"
//  var emailServerPort="80"
//  var emailServerUserName="test@server.scalachan.com"
//  var emailServerUserPassword="TESTtest123"
//  var emailSendAddress="test@server.scalachan.com"
  var emailSpareMilliTime=1000

  //分钟
  var dashboardTimertaskSpareTime= 60 * 3 //3 hour

  var resourcePrefixPath = "/var/platform/resources"

  var weChatUploadPath = "/var/platform/resources/wechat"

  def beginLoadProperties(rootPath: String): Unit = {
    loadConfigerations(loadProperties(rootPath))

    ConfigurationHelper.rootPath = rootPath
  }

  def kafkaZookeeperAddress: String = s"$kafkaZooKeeperIp:$kafkaZooKeeperPort"
  def kafkaAddress: String = s"$kafkaIp:$kafkaPort"


  private def loadProperties(rootPath: String): Properties = {
    val properties: Properties = new Properties()
    logger.info(s"*********************beginLoadProperties rootPath:$rootPath/config.properties")
    properties.load(new FileInputStream(rootPath + "/config.properties"))
    properties
  }

  private def loadConfigerations(properties: Properties): Unit = {
    applicationPort      = properties.getProperty("applicationPort", applicationPort.toString).toInt
    zohoCrmAddress       = properties.getProperty("zohoCrmIpAddress", zohoCrmAddress)
    hbaseZookeeperIp     = properties.getProperty("hbaseZookeeperIp", hbaseZookeeperIp)
    hbaseZookeeperPort   = properties.getProperty("hbaseZookeeperPort",  hbaseZookeeperPort.toString).toInt
    esAddr = properties.getProperty("esAddr")

    moduleTable          = properties.getProperty("moduleTable", moduleTable)
    moduleTableFamily    = properties.getProperty("moduleTableFamily", moduleTableFamily)

    statusTable          = properties.getProperty("statusTable", statusTable)
    statusFamily         = properties.getProperty("statusFamily", statusFamily)
    statusTableFamilyNo  = properties.getProperty("statusTableFamilyNo", statusTableFamilyNo)

    zohoIndex            = properties.getProperty("zohoIndex", zohoIndex)
    zohoIndexFamily      = properties.getProperty("zohoIndexFamily", zohoIndexFamily)

    cookieMappingTable   = properties.getProperty("cookieMappingTable", cookieMappingTable)
    cookieMappingFamily  =  properties.getProperty("cookieMappingFamily", cookieMappingFamily)

    prismRealtimeDataTable  = properties.getProperty("prismRealtimeDataTable", prismRealtimeDataTable)
    prismFamily          = properties.getProperty("prismFamily", prismFamily)

    allLabelTable        = properties.getProperty("allLabelTable", allLabelTable)
    allLabelTableFamily  = properties.getProperty("allLabelTableFamily", allLabelTableFamily)

    labelManagerTable    = properties.getProperty("labelManager", labelManagerTable)
    labelManagerFamily   = properties.getProperty("labelManagerFamily", labelManagerFamily)

    baiduTongjiTable     = properties.getProperty("baiduTongjiTable", baiduTongjiTable)
    baiduTongjiFamily    = properties.getProperty("baiduTongjiFamily", baiduTongjiFamily)

    prismDataTable       = properties.getProperty("prismDataTable", prismDataTable)
    prismDataFamily      = properties.getProperty("prismDataFamily", prismDataFamily)

    prismCursorTable     = properties.getProperty("prismCursorTable", prismCursorTable)
    prismCursorFamily    = properties.getProperty("prismCursorFamily", prismCursorFamily)

    prismTopic           = properties.getProperty("prismTopic", prismTopic)
    kafkaZooKeeperIp     = properties.getProperty("kafkaZookeeperIp", kafkaZooKeeperIp)
    kafkaZooKeeperPort   = properties.getProperty("kafkaZookeeperPort", kafkaZooKeeperPort.toString).toInt
    kafkaIp              = properties.getProperty("kafkaIp", kafkaIp)
    kafkaPort            = properties.getProperty("kafkaPort", kafkaPort.toString).toInt
    phoenixZnode         = properties.getProperty("phoenixZnode", phoenixZnode)
    hbaseZnodeParent     = properties.getProperty("hbaseZnodeParent", hbaseZnodeParent)
    prismZookeeperIp     = properties.getProperty("prismZookeeperIp", prismZookeeperIp)
    prismZookeeperPort   = properties.getProperty("prismZookeeperPort", prismZookeeperPort.toString).toInt
    prismZnode           = properties.getProperty("prismZnode", prismZnode)
    tempUserToken        = properties.getProperty("userTokenTemp", tempUserToken)


    isTest               = properties.getProperty("isTest", isTest.toString).toBoolean
    isSyncPrism          = properties.getProperty("isSyncPrism", isSyncPrism.toString).toBoolean
    isSyncZoho           = properties.getProperty("isSyncZoho", isSyncZoho.toString).toBoolean
    isSyncBaidu           = properties.getProperty("isSyncBaidu", isSyncBaidu.toString).toBoolean
    isAlterTable         = properties.getProperty("isAlterTable", isAlterTable.toString).toBoolean
    syncDataFromPrismInStartSystem = properties.getProperty("syncDataFromPrismInStartSystem", syncDataFromPrismInStartSystem.toString).toBoolean

    sfTest = properties.getProperty("sfTest", "false").toBoolean

    msSFDailySyncON = properties.getProperty("msSFDailySyncON", "true").toBoolean
    msSFDailySyncOrg = properties.getProperty("msSFDailySyncOrg", "penny")
    msSFDailyClientID = properties.getProperty("msSFDailyClientID", "3MVG9Y6d_Btp4xp7oyJw27xIVRLywJiCrmGH2zz2k9.zlkAgsPTHRknAq7QwOfQlOEOWxvoQ5SyhoP_PQIZOw")
    msSFDailyClientSecret = properties.getProperty("msSFDailyClientSecret", "1829944655512610073")
    msSFDailyUserAccount = properties.getProperty("msSFDailyUserAccount", "DataSynchronism@maserati.com")
    msSFDailyUserPwd = properties.getProperty("msSFDailyUserPwd", "maserati2017")

    OS_VERSION         = properties.getProperty("OS_VERSION", OS_VERSION)
    DEVICE_TYPE        = properties.getProperty("DEVICE_TYPE", DEVICE_TYPE)
    CLIENT_VERSION     = properties.getProperty("CLIENT_VERSION ", CLIENT_VERSION)
    LOGIN_URL          = properties.getProperty("LOGIN_URL", LOGIN_URL)
    PROFILE_URL        = properties.getProperty("PROFILE_URL", PROFILE_URL)
    DATA_URL           = properties.getProperty("DATA_URL", DATA_URL)
    PUBLIC_KEY         = properties.getProperty("PUBLIC_KEY", PUBLIC_KEY)

    mysqlAddress         = properties.getProperty("mysqlAddress", mysqlAddress)
    mysqlAccount         = properties.getProperty("mysqlAccount", mysqlAccount)

    mysqlPassword  = properties.getProperty("mysqlPassword", mysqlPassword)
//    if (properties.getProperty("mysqlPassword") != null)
//      {
//        if (!netAddress.equals("russ-X555LI"))
//          mysqlPassword  = properties.getProperty("mysqlPassword", mysqlPassword)
//        else
//          mysqlPassword  = "123456"
//      }

    prismAddress       = properties.getProperty("prismAddress", prismAddress)
    isMapCount         = properties.getProperty("isMapCount", isMapCount.toString).toBoolean

    ip2GeoTable         = properties.getProperty("ip2Geo", ip2GeoTable)
    ip2GeoTableFamily   = properties.getProperty("ip2GeoFamily", ip2GeoTableFamily)

    localIpData         = properties.getProperty("ipData", localIpData)
    getGeoLocal       = properties.getProperty("getGeoLocal", "false").toBoolean
    isSparkDataServiceTrigger = properties.getProperty("issparkDataServiceTrigger", "false").toBoolean
    sparkDataServiceUrl = properties.getProperty("sparkDataServiceUrl", sparkDataServiceUrl)
    rtbAsiaToken        = properties.getProperty("rtbAsiaToken", "3562ea65b0fe82e39ab9a8233d741ea1")
    hiveConnectionPath = properties.getProperty("hiveConnectionPath", "jdbc:hive2://192.168.3.2:10000/default")
    nexusSqlHbaseFamily = properties.getProperty("nexusSqlHbaseFamily", "cf")

    if (properties.getProperty("excelOutputFileDir") != null)
      excelOutputFileDir = properties.getProperty("excelOutputFileDir")

    if (properties.getProperty("fileUploadDir") != null)
      fileUploadDir = properties.getProperty("fileUploadDir")

    if (properties.getProperty("labelUseCachedResult") != null)
      labelUseCachedResult = properties.getProperty("labelUseCachedResult").toBoolean
    if (properties.getProperty("labelCoverScopeCalculateFrequency") != null)
      labelCoverScopeCalculateFrequency = properties.getProperty("labelCoverScopeCalculateFrequency").toInt
    if (properties.getProperty("labelCoverScopeActorStartHour") != null)
      labelCoverScopeActorStartHour = properties.getProperty("labelCoverScopeActorStartHour").toInt
    if (properties.getProperty("labelTriggerCalculatingWhenLaunch") != null)
      labelTriggerCalculatingWhenLaunch = properties.getProperty("labelTriggerCalculatingWhenLaunch").toBoolean
    if (properties.getProperty("labelCalculateGraphDataTime") != null)
      labelCalculateGraphDataTime = properties.getProperty("labelCalculateGraphDataTime").toInt
    if (properties.getProperty("labelCoverScopeCalculateActorNumber") != null)
      labelCoverScopeCalculateActorNumber = properties.getProperty("labelCoverScopeCalculateActorNumber").toInt

    labelIQResultDB = properties.getProperty("labelIQResultDB", "CDP")
    labelIQResultDBTable = properties.getProperty("labelIQResultDBTable", "KMeansPCAEntity")

    if (properties.getProperty("syncPrismCustomerEventData") != null)
      syncPrismCustomerEventData = properties.getProperty("syncPrismCustomerEventData").toBoolean

    if (properties.getProperty("sparkUser")!=null)
      sparkUser = properties.getProperty("sparkUser")

    if (properties.getProperty("sparkHdfsPath")!=null)
      sparkHdfsPath = properties.getProperty("sparkHdfsPath")

    if (properties.getProperty("iqModulesURI")!=null)
      iqModulesURI= properties.getProperty("iqModulesURI")

    isFakeOverView = properties.getProperty("ifFakeOverView", "false").toBoolean
    shouldInitHiveTable = properties.getProperty("shouldInitHiveTable", "false").toBoolean
    syncCustomerdata = properties.getProperty("syncCustomerdata", "false").toBoolean

    remoteHolderAddr = properties.getProperty("remoteHolderAddr", "akka.tcp://data_service@192.168.1.199:2046/user/holder")
    remoteCalculationHolderAddr = properties.getProperty("remoteCalculationHolderAddr", remoteCalculationHolderAddr)

    if (properties.getProperty("awsAccessId")!=null)
      awsAccessId =  properties.getProperty("awsAccessId")
    if (properties.getProperty("awsAccessKey")!=null)
      awsAccessKey = properties.getProperty("awsAccessKey")

    if (properties.getProperty("labelRemoteCalActorAddr")!=null)
      labelRemoteCalActorAddr = properties.getProperty("labelRemoteCalActorAddr")

    newLabelRemoteCalActorAddr = properties.getProperty("newLabelRemoteCalActorAddr", newLabelRemoteCalActorAddr)

    labelCalculatingSwitchToSparkSql = properties.getProperty("labelCalculatingSwitchToSparkSql", "false").toBoolean
    prepareForLabelSparkSQL = properties.getProperty("prepareForLabelSparkSQL", "false").toBoolean
    convertExcelPhoenixEncoding = properties.getProperty("convertExcelPhoenixEncoding", "false").toBoolean
    labelCustomerPotraitRemoteActorAddr  = properties.getProperty("labelCustomerPotraitRemoteActorAddr",
      labelCustomerPotraitRemoteActorAddr)

    convertExcelPhoenixEncodingDone = properties.getProperty("convertExcelPhoenixEncodingDone", "false").toBoolean
    closeLabelRecalculating = properties.getProperty("closeLabelRecalculating", "true").toBoolean

    audienceRemoteCalActorAddr = properties.getProperty("audienceRemoteCalActorAddr", audienceRemoteCalActorAddr)
    closeAudienceRecalculating = properties.getProperty("closeAudienceRecalculating", "true").toBoolean
    audienceCoverScopeActorStartHour = properties.getProperty("audienceCoverScopeActorStartHour", "4").toInt

    wechatAppSecret = properties.getProperty("wechatAppSecret", wechatAppSecret)
    wechatAppId = properties.getProperty("wechatAppId", wechatAppId)
    wechatToken = properties.getProperty("wechatToken", wechatToken)
    wechatKey = properties.getProperty("wechatKey", wechatKey)

    aliCloudSMSTable = properties.getProperty("aliCloudSMSTable", aliCloudSMSTable)
    aliCloudSMSColumnFamily = properties.getProperty("aliCloudSMSColumnFamily", aliCloudSMSColumnFamily)
    aliCloudAccessKeyId = properties.getProperty("aliCloudAccessKeyId", aliCloudAccessKeyId)
    aliCloudAccessKeySecret = properties.getProperty("aliCloudAccessKeySecret", aliCloudAccessKeySecret)
    aliCloudSMSReceiptQueueName = properties.getProperty("aliCloudSMSReceiptQueueName", aliCloudSMSReceiptQueueName)
    aliCloudSMSReplyQueueName = properties.getProperty("aliCloudSMSReplyQueueName", aliCloudSMSReplyQueueName)

    if(properties.getProperty("ihuyiAccount")!=null)
      ihuyiAccount = properties.getProperty("ihuyiAccount")
    if(properties.getProperty("ihuyiPassword")!=null)
      ihuyiPassword = properties.getProperty("ihuyiPassword")

    mtConnectRemoteActor = properties.getProperty("mtConnectRemoteActor", mtConnectRemoteActor)

//    emailSendAddress =  properties.getProperty("emailSendAddress", emailSendAddress)
//    emailServerHost = properties.getProperty("emailServerHost", emailServerHost)
//    emailServerUserName = properties.getProperty("emailServerUserName", emailServerUserName)
//    emailServerUserPassword = properties.getProperty("emailServerUserPassword", emailServerUserPassword)
    resourcePrefixPath = properties.getProperty("resourcePrefixPath", resourcePrefixPath)
    weChatUploadPath = properties.getProperty("weChatUploadPath", weChatUploadPath)

    trackingRecordTable

  }
}

/**
  * load all config file
  */
object RpcAkkaServiceConfig {
  /*private val fileConf = ConfigFactory.parseFile(new File("./application.conf"))
  private val online = ConfigFactory.parseResourcesAnySyntax("online")
  private val local = ConfigFactory.parseResourcesAnySyntax("local")
  private val develop = ConfigFactory.parseResourcesAnySyntax("application") //application is also develop environment in this project
  private val default = ConfigFactory.load() //default environment

  private val myConfig = fileConf.withFallback(online).withFallback(local).withFallback(develop).resolve()
  private val combinedConfig = myConfig.withFallback(default)

  val akkaConfig: Config = combinedConfig.withOnlyPath("akka")

  println("====================Akka Config Begin=========================")
  printConf(myConfig)
  println("====================Akka Config End=========================")

  def prefixConfig(prefix: String, srcConfig: Config): Config = ConfigFactory.parseString(prefix).withFallback(srcConfig)
  def printConf(config: Config): Unit = println(config.root().render(ConfigRenderOptions.concise().setFormatted(true).setJson(true)))*/
}

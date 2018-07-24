package ip

import java.io.InputStream
import java.util

import org.slf4j.LoggerFactory
import sun.misc.IOUtils

import scala.util.control.NonFatal


/**
  * Created by Sirius on 2017/4/14.
  */
case class IpResolutionResult(ip: String, country: String, region: String, city: String, area: String)
case class UaResolutionResult(device: String, os: String, browser: String, browserVersion: String)
case class ResponseData(ip:String,
                        country: String,
                        area: String,
                        region: String,
                        city: String,
                        county: String,
                        isp: String,
                        country_id: String,
                        area_id: String,
                        region_id: String,
                        city_id: String,
                        county_id: String,
                        isp_id: String
                        )

case class TBResponse(code: String, data: ResponseData)


object IpResolutionService{
  private val logger = LoggerFactory.getLogger(this.getClass)


  /*def resolveFromLocalFile(ips: Seq[String]): List[IpResolutionResult] = {
    getGeoFromLocal(ips)
  }

  def getGeoFromLocal(ip: String): IpResolutionResult = {
    geoLoan(geoInfo(ip)(_).get)
  }*/

  /*def getGeoFromLocal(ips: Seq[String]): List[IpResolutionResult] = {
    geoLoan(file => {
      val rst = ips.map { ip =>
        geoInfo(ip)(file)
      }.filter(_.isSuccess).map(_.get).toList
      rst
    })
  }*/

  /**
    *
    */
 /* private def geoLoan[T](action: RandomAccessFile => T): T = {
    val url=getClass.getResource("/my_model.ser.gz")

    val file = new RandomAccessFile(DataServiceConfig.dataServiceCf.localIpData, "r")
    try {
      action(file)
    } catch {
      case NonFatal(e) =>
        logger.info("geo ip.data access fail - " + e.getStackTrace.mkString("\n"))
        throw e
    } finally {
      file.close()
    }
  }*/

  /*private def geoInfo(ip: String)(file: RandomAccessFile): Try[IpResolutionResult] = Try{
    val ipString = ip.split("[.]").map(s => s.toInt).filter(x => x >= 0 && x <= 255)
    var ret = IpResolutionResult(ip, "", "", "", "")
    if (ipString.length.equals(4)) {
      val loc = ((1 << 16) * ipString(0) + (1 << 8) * ipString(1) + ipString(2)) * 5
      file.seek(loc)
      val country_id = file.readByte.toChar & 0xff
      val region_id = file.readByte.toChar & 0xff
      val cid1 = file.readByte.toChar & 0xff
      val cid2 = file.readByte.toChar & 0xff
      val cid = cid1 * 256 + cid2
      val country = IpGeoConst.country(country_id)
      val region = IpGeoConst.region(region_id)
      val city = IpGeoConst.city(cid)
      ret = IpResolutionResult(ip, country, region, city, "")
    }
    ret
  }*/

  def readBytesFromResource(path: String) = {
    val stream : InputStream = getClass.getResourceAsStream(path)
    IOUtils.readFully(stream, -1, false)
  }

//  private val geoByteArray = FileHelper.readBytesFromResource("/assets/ip.data")//Files.readAllBytes(Paths.get(DataServiceConfig.dataServiceCf.localIpData))
private val geoByteArray = readBytesFromResource("/assets/ip.data")

  def geoDataFromMem(ip: String): util.HashMap[String, String] = {
    val map = new util.HashMap[String, String]()
    try {
      val ipString = ip.split("[.]").map(s => s.toInt).filter(x => x >= 0 && x <= 255)
      if (ipString.length.equals(4)) {
        val loc = ((1 << 16) * ipString(0) + (1 << 8) * ipString(1) + ipString(2)) * 5
        println(loc + "***")
        val country_id = geoByteArray(loc).toChar & 0xff
        val region_id = geoByteArray(loc + 1).toChar & 0xff
        val cid1 = geoByteArray(loc + 2).toChar & 0xff
        val cid2 = geoByteArray(loc + 3).toChar & 0xff
        val cid = cid1 * 256 + cid2
        val country = IpGeoConst.country(country_id)
        val region = IpGeoConst.region(region_id)
        val city = IpGeoConst.city(cid)
        map.put("geoip.country_name", country)
        map.put("geoip.city_name", city)
        map.put("geoip.region_name", region)
        map.put("geoip.ip", ip)
      }
    } catch {
      case NonFatal(e) =>
        logger.warn(s"ip parse fail: ${ip}")
    }
    map
  }
}

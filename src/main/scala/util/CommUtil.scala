package util

import java.io.{File, PrintWriter, StringWriter}

/**
  * Created by baibing.shang on 2017/8/9.
  */
final object CommUtil {
  /**
    * 转换md5
    * @param value
    * @return
    */
  /*def md5(s: String) = {
    val m = java.security.MessageDigest.getInstance("MD5")
    val b = s.getBytes("UTF-8")
    m.update(b, 0, b.length)
    new java.math.BigInteger(1, m.digest()).toString(16)
  }*/

  /**
    * hive 正取余方法  pmod
    * @param value hash值
    * @param mod 取余
    * @return
    */
  def pmod (value: Int, mod: Int): Int = {
    val pvalue = ( value % mod + mod ) % mod
    pvalue
  }

  def md5(value: String) = {
    val hexDigits = Array[Char]('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
    val md5handle = java.security.MessageDigest.getInstance("MD5")
    val encrypt = md5handle.digest(value.getBytes)
    val b = new StringBuilder(32)
    for (i <- 0.to(15)) {
      b.append(hexDigits(encrypt(i) >>> 4 & 0xf)).append(hexDigits(encrypt(i) & 0xf))
    }
    b.mkString
  }

  /**
    * scala 将异常信息完成输出到日志中
    * @param e
    */
  /*def printStackTraceStr(e: Exception, str: String) = {
    val writer = new PrintWriter(new File("ExceptionMsg_"+ DateUtil.getToday() +".txt"))
    val sw:StringWriter = new StringWriter()
    val pw:PrintWriter = new PrintWriter(sw)
    e.printStackTrace(pw)
    writer.println("======>>printStackTraceStr Exception: " + e.getClass())
    writer.append("==>" + sw.toString () + "\n==>date=" + DateUtil.getNowDate() + "\n==>rowkey=" + str)
    writer.flush()
    writer.close()
  }*/
}

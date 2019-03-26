package util

/**
  * Created by baibing.shang on 2018/1/10.
  */
final object MD5 {
  /**
    * 转换md5
    * @param value
    * @return
    */
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

  def main(args: Array[String]) {
    println(md5("123456"))
  }
}

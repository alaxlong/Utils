package test

import scala.collection.mutable
import scala.util.Try


/**
  * Created by Administrator on 2018/8/17.
  */
class stu{
  val age = 25
  val name = 26
}
case class DD(data: Seq[Byte]){
  def length = data.length

}
object DD {

  private val Alphabet = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
  private val Base = BigInt(58)

  def decodeToBigInteger(input: String): BigInt =

    input.foldRight((BigInt(0), input.length - 1)) { case (ch, (bi, i)) =>
      val alphaIndex = Alphabet.indexOf(ch)
        .ensuring(_ != -1, "wrong char")
      (bi + BigInt(alphaIndex) * Base.pow(input.length - 1 - i), i - 1)
    }._1

  def main(args: Array[String]): Unit = {
    val bytes = new Array[Byte](5)
    bytes.update(0, 1)
    bytes.update(1, 2)
    bytes.update(2, 3)
    bytes.update(3, 4)


//    println(bytes.dropRight(3).apply(0))

    val list = List(1111,2,3,4,5)
    println(list.head)
//    require(bytes.length < 0, "Error -------")

    val input = Some()
    println(input.getOrElse("a"))


    println(0x00.toByte)

    val saa: Map[String, String] = Map("s" -> "d")


    val s: Option[String] = saa.get("s")
    println(s.get)

    val myMap: Map[String, String] = Map("key1" -> "value")
    val value1: Option[String] = myMap.get("key1")
    val value2: Option[String] = myMap.get("key2")


    /*println(Array.fill[Byte](10)(0).apply(5))
    val s: Byte = 1;
    val s2: Byte = 1;
    val s3: Byte = 2;


    val c: Seq[Byte] = Seq(s, s2, s3)
    println(Array.fill(32 - 31)(0.toByte).apply(0))
    val arr = Array(10, 10, 20)

    val bytes = new Array[Byte](5)
    bytes.update(1, 'a')
    bytes.update(2, 'c')
    val ss= 2
    println(Try(ss))

    println(arr ++ List(1, 2))
    println("ssccss".stripPrefix("ss"))

    println(BigInt(58))*/

  }

}

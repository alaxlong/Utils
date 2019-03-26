package util

import org.dom4j.DocumentHelper

import scalaj.http.Http


object IhuyiClient {
  val ApiUrl = "http://api.yx.ihuyi.com/webservice/sms.php?method=Submit"
  def send(numbers: List[String], content: String, stime: Option[String] = None): String = {
    val response = Http(ApiUrl).postForm.param("account", ConfigurationHelper.ihuyiAccount)
      .param("password", ConfigurationHelper.ihuyiPassword)
      .param("mobile", numbers.mkString(","))
      .param("content", content)
      .param("stime", stime.getOrElse(""))
      .asString
    val doc = DocumentHelper.parseText(response.body)
    val root = doc.getRootElement

    val code = root.elementText("code")
    val msg = root.elementText("msg")
    val smsid = root.elementText("smsid")

    if(code == "2") {
      smsid
    }
    else {
      throw new Exception(msg)
    }
  }

  def main(args: Array[String]): Unit = {
    println(IhuyiClient.send(List("18516129614","18662813449"), "尊敬的会员，第五次，推送。退订回TD【Chinapex】"))
  }
}

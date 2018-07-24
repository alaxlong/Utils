package util

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

/**
  * Created by baibing.shang on 2017/8/9.
  */
object DateTools {

  /**
    * 时间戳转换成日期 时间格式 yyyy-MM-dd HH:mm:ss
    * 时间戳单位：str:秒
    * @param str
    * @return
    */
  def DateFormat(str: String): String = {
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date: String = sdf.format(new Date(str.toLong * 1000L))
    date
  }

  /**
    * 获取30天后的日期，用于expireAt字段，设置数据过期时间
    * Date
    * @return
    */
  def getDateAfter(): Date ={
    val cal: Calendar = Calendar.getInstance()
    cal.setTime(new Date())
    cal.add(Calendar.DATE, 30)
    cal.getTime
  }

  /**
    * 获取当前时间
    *yyyy-MM-dd HH:mm:ss
    * @return
    */
  def getNowDate(): String = {
    val now: Date = new Date()
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val nowDate = dateFormat.format( now )
    nowDate
  }

  /**
    * 获取当前日期
    *yyyy-MM-dd
    * @return
    */
  def getToday(): String = {
    val now: Date = new Date()
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val nowDate = dateFormat.format( now )
    nowDate
  }

  /**
    * 获取昨天时间
    * yyyy-MM-dd
    * @return
    */
  def getYesterday(): String = {
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DATE, -1)
    val yesterday = dateFormat.format(cal.getTime())
    yesterday
  }

  /**
    * 获取本周一的日期
    * yyyy-MM-dd
    * @return
    */
  def getNowWeekStart(): String = {
    val cal: Calendar = Calendar.getInstance()
    val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    val period: String = df.format(cal.getTime())
    period
  }

  /**
    * 获取本周末的时间
    * yyyy-MM-dd
    * @return
    */
  def getNowWeekEnd(): String = {
    val cal: Calendar = Calendar.getInstance()
    val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    //这种输出的是上个星期周日的日期，因为老外把周日当成第一天
    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    //增加一个星期，才是我们中国人的本周日的日期
    cal.add(Calendar.WEEK_OF_YEAR, 1)
    val period: String = df.format(cal.getTime())
    period
  }

  /**
    * 本月的第一天
    * yyyy-MM-dd
    * @return
    */
  def getNowMonthStart(): String = {
    val cal: Calendar = Calendar.getInstance()
    val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    cal.set(Calendar.DATE, 1)
    val period: String = df.format(cal.getTime())//本月第一天
    period
  }

  /**
    * 本月的最后一天
    * @return
    */
  def getNowMonthEnd(): String={
    val cal: Calendar = Calendar.getInstance()
    val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    cal.set(Calendar.DATE, 1)
    cal.roll(Calendar.DATE, -1)
    val period: String = df.format(cal.getTime())//本月最后一天
    period
  }

}

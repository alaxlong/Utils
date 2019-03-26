/*
import java.text.SimpleDateFormat
import java.util.Date

import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest
import com.aliyuncs.profile.DefaultProfile
import com.chinapex.dao.{ActivityDao, AliCloudSMSDao}
import com.chinapex.entities.{ActivityEntity, AliCloudSMSEntity}
import com.chinapex.helpers.ConfigurationHelper
import com.chinapex.helpers.mappers.JsonMapper
import org.slf4j.LoggerFactory
import com.aliyun.mns.model.Message
import com.alicom.mns.tools.{DefaultAlicomMessagePuller, MessageListener}
import com.chinapex.clients.dbclients.HbaseClientOfOrg
import com.chinapex.const.ActivityStatusConst
import com.google.gson.Gson


case class AliCloudSmsReport(phone_number: String, success: Boolean, biz_id: String, out_id: Option[String], send_time: String, report_time: String, err_code: String, err_msg: String, sms_size: String)
case class AliCloudSmsReply(phone_number: String, content: String, send_time: String, dest_code: Option[String], sequence_id: Long, sp_id: Option[String])


object AliCloudSMSService {

  val logger = LoggerFactory.getLogger(this.getClass)
  val hbaseClient = HbaseClientOfOrg()

  def sendSms(activityId: String, phones: List[String], signName: String, templateId: String, upLinkId: String, templateData: Option[Map[String, String]]): Unit = {
    try{
      logger.info(s"SendSMS called. phone count = ${phones.length}.")
      val product = "Dysmsapi"
      val domain = "dysmsapi.aliyuncs.com"

      val profile = DefaultProfile.getProfile("cn-hangzhou", ConfigurationHelper.aliCloudAccessKeyId, ConfigurationHelper.aliCloudAccessKeySecret)
      DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain)
      val acsClient = new DefaultAcsClient(profile)

      val smsRequest = new SendSmsRequest()
      smsRequest.setPhoneNumbers(phones.mkString(","))
      smsRequest.setSignName(signName)
      smsRequest.setTemplateCode(templateId)
      templateData.fold()(data => {
        smsRequest.setTemplateParam(JsonMapper.to(data))
      })
      smsRequest.setOutId(upLinkId)
      //    smsRequest.setSmsUpExtendCode(upLinkId)
      AliCloudSMSDao.getByActivityId(activityId).fold({
        logger.error(s"No such activity. Cannot set smsUpExtendCode, abort. activityId = $activityId. ")
      })(smsss => {
        val returnId = smsss.id.toString
        smsRequest.setSmsUpExtendCode(returnId)
        smsRequest.setOutId(returnId)
      })

      val response = acsClient.getAcsResponse(smsRequest)
      val code = response.getCode
      val message = response.getMessage
      val requestId = response.getRequestId
      val bizId = response.getBizId
      if(code != "OK"){
        ActivityDao.getById(activityId).fold({
          logger.error(s"No such activity. id = $activityId.")
        })(act => {
          val entity = act.toEntity
          entity.message = Some(message)
          entity.status = ActivityStatusConst.Fail.toString
          entity.update()
        })
      }
      logger.info(s"SMS returned status = code: $code, message: $message, requestId: $requestId, biaId: $bizId.")
      AliCloudSMSDao.getByActivityId(activityId).fold({
        logger.error(s"No such activity. activityId = $activityId.")
      })(sms => {
        sms.requestId = requestId
        sms.outId = upLinkId
        sms.sign = signName
        sms.templateId = templateId
        sms.templateData = Some(templateData.fold("")(z => z.map(y => y._1 + ":" + y._2).mkString(";")))
        sms.updateEntity()
      })
    } catch {
      case e: Throwable =>
        logger.error(s"Failed to send SMS. reason = ${e.getMessage}.")
        ActivityDao.getById(activityId).fold({
          logger.error(s"No such activity. id = $activityId.")
        })(act => {
          val entity = act.toEntity
          entity.message = Some(e.getMessage)
          entity.status = ActivityStatusConst.Fail.toString
          entity.update()
        })
    }

  }

  object SmsDistributionReceipt extends MessageListener{
    override def dealMessage(message: Message): Boolean = {
      val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      logger.info(s"Received SMS receipt from AliCloud MNS, time = ${sdf.format(new Date())}. messageId = ${message.getMessageId}.")
      try{
        val retData = JsonMapper.from[AliCloudSmsReport](message.getMessageBodyAsString)
//        val retData = JsonMapper.from[Map[String, String]](message.getMessageBodyAsString)

        val receipt = Map(
          "phone" -> retData.phone_number,
          "success" -> (if(retData.success) "true" else "false"),
          "bizId" -> retData.biz_id,
          "outId" -> retData.out_id.fold("0")(z => z),
          "sendTime" -> retData.send_time,
          "reportTime" -> retData.report_time,
          "errCode" -> retData.err_code,
          "errMsg" -> retData.err_msg
        )

//        val receipt = Map(
//          "phone" -> retData.getOrElse("phone_number", ""),
//          "success" -> retData.getOrElse("success", "false"),
//          "bizId" -> retData.getOrElse("biz_id", ""),
//          "outId" -> retData.getOrElse("out_id", ""),
//          "sendTime" -> retData.getOrElse("send_time", ""),
//          "reportTime" -> retData.getOrElse("report_time", ""),
//          "errCode" -> retData.getOrElse("err_code", ""),
//          "errMsg" -> retData.getOrElse("err_msg", "")
//        )
        val k = "receipt_" + retData.out_id + "_" + retData.phone_number
        hbaseClient.insert(ConfigurationHelper.aliCloudSMSTable, k, ConfigurationHelper.aliCloudSMSColumnFamily, receipt)
        val smsId = receipt.get("outId").fold(0)(outId => {
          if (outId.forall(Character.isDigit)) {
            outId.toInt
          }
          else{
            0
          }
        })
        AliCloudSMSDao.getById(smsId).fold({
          logger.error(s"No such task. outId = ${receipt}.")
        })(z => {
          val activityId = z.activityId
          ActivityDao.getById(activityId).fold({
            logger.error(s"No such activity. id = $activityId")
          })(activity => {
            val entity = activity.toEntity
            entity.status = ActivityStatusConst.Success.toString
            entity.update()
          })
        })
        true
      } catch {
        case e: Throwable =>
          logger.error(s"Error processing AliCloud SMS receipt. reason = ${e.getMessage}.")
          logger.info(s"message content = ${message.getMessageBodyAsString}")
          false
      }
    }
  }

  object SmsReply extends MessageListener{
    override def dealMessage(message: Message): Boolean = {
      val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      logger.info(s"Received SMS reply from AliCloud MNS, time = ${sdf.format(new Date())}, messageId = ${message.getMessageId}.")
      try{
        val retData = JsonMapper.from[AliCloudSmsReply](message.getMessageBodyAsString)
//        logger.info(s"smsBody = ${message.getMessageBodyAsString}.")
        val reply = Map(
          "phone" -> retData.phone_number,
          "content" -> retData.content,
          "sendTime" -> retData.send_time,
          "destCode" -> retData.dest_code.fold("")(z => z),
          "sequenceId" -> retData.sequence_id.toString
        )
        val k = "reply_" + retData.dest_code.fold("0")(z => z) + "_" + retData.phone_number
        hbaseClient.insert(ConfigurationHelper.aliCloudSMSTable, k, ConfigurationHelper.aliCloudSMSColumnFamily, reply)
        true
      } catch {
        case e: Throwable =>
          logger.error(s"Error process AliCloud SMS reply. reason = ${e.getMessage}.")
          false
      }
    }
  }

  def messagePuller():Unit = {
    if(!hbaseClient.isTableExist(ConfigurationHelper.aliCloudSMSTable)){
      hbaseClient.createTable(ConfigurationHelper.aliCloudSMSTable, ConfigurationHelper.aliCloudSMSColumnFamily)
    }

    val receiptPuller = NexusAliCloudMessagePuller(
      ConfigurationHelper.aliCloudAccessKeyId,
      ConfigurationHelper.aliCloudAccessKeySecret,
      "SmsReport",
      ConfigurationHelper.aliCloudSMSReceiptQueueName,
      SmsDistributionReceipt
    )

    val replyPuller = NexusAliCloudMessagePuller(
      ConfigurationHelper.aliCloudAccessKeyId,
      ConfigurationHelper.aliCloudAccessKeySecret,
      "SmsUp",
      ConfigurationHelper.aliCloudSMSReplyQueueName,
      SmsReply
    )

    receiptPuller.start()
    replyPuller.start()
//    try{
//      receiptPuller.setConsumeMinThreadSize(1)
//      receiptPuller.setConsumeMaxThreadSize(1)
//      receiptPuller.setThreadQueueSize(100)
//      receiptPuller.setPullMsgThreadSize(1)
//      receiptPuller.openDebugLog(false)
//
//      replyPuller.setConsumeMinThreadSize(1)
//      replyPuller.setConsumeMaxThreadSize(1)
//      replyPuller.setThreadQueueSize(100)
//      replyPuller.setPullMsgThreadSize(1)
//      replyPuller.openDebugLog(false)
//
//      val messageTypeReceipt = "SmsReport"
//      val messageTypeReply = "SmsUp"
//
//      val receiptQueueName = ConfigurationHelper.aliCloudSMSReceiptQueueName
//      val replyQueueName = ConfigurationHelper.aliCloudSMSReplyQueueName
//
//      if(receiptQueueName.nonEmpty){
//        receiptPuller.startReceiveMsg(ConfigurationHelper.aliCloudAccessKeyId, ConfigurationHelper.aliCloudAccessKeySecret, messageTypeReceipt, receiptQueueName, SmsDistributionReceipt)
//      }
//      if(replyQueueName.nonEmpty){
//        replyPuller.startReceiveMsg(ConfigurationHelper.aliCloudAccessKeyId, ConfigurationHelper.aliCloudAccessKeySecret, messageTypeReply, replyQueueName, SmsReply)
//      }
//    } catch {
//      case e: Throwable =>
//        receiptPuller.stop()
//        replyPuller.stop()
//        logger.error(s"Failed to start aliCloud MNS puller. reason = ${e.getMessage}. Pullers had been stopped.")
//    }

  }

}
*/

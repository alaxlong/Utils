package test

import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging
import akka.actor.ActorSystem
import scala.concurrent.Future
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.pipe
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Administrator on 2018/8/14.
  */
object Example13 {
  def main(args: Array[String]) {
    val _system = ActorSystem("Send-And-Receive-Future")
    val combineActor = _system.actorOf(Props[CombineActor],name="CombineActor")
    combineActor ! 12345
    Thread.sleep(5000)
    _system.stop(combineActor);
  }



  //消息：个人基础信息
  case class BasicInfo(id:Int,val name:String, age:Int)
  //消息：个人兴趣信息
  case class InterestInfo(id:Int,val interest:String)
  //消息: 完整个人信息
  case class Person(basicInfo: BasicInfo,interestInfo: InterestInfo)


  //基础信息对应Actor
  class BasicInfoActor extends Actor{
    val log = Logging(context.system, this)
    def receive = {
      //处理送而来的用户ID，然后将结果发送给sender（本例中对应CombineActor）
      case id:Int ⇒log.info("id="+id);sender!new BasicInfo(id,"John",19)
      case _      ⇒ log.info("received unknown message")
    }
  }

  //兴趣爱好对应Actor
  class InterestInfoActor extends Actor{
    val log = Logging(context.system, this)
    def receive = {
      //处理发送而来的用户ID，然后将结果发送给sender（本例中对应CombineActor）
      case id:Int ⇒log.info("id="+id);sender!new InterestInfo(id,"足球")
      case _      ⇒ log.info("received unknown message")
    }
  }

  //Person完整信息对应Actor
  class PersonActor extends Actor{
    val log = Logging(context.system, this)
    def receive = {
      case person: Person =>log.info("Person="+person)
      case _      ⇒ log.info("received unknown message")
    }
  }


  class CombineActor extends Actor{
    implicit val timeout = Timeout(5 seconds)
    val basicInfoActor = context.actorOf(Props[BasicInfoActor],name="BasicInfoActor")
    val interestInfoActor = context.actorOf(Props[InterestInfoActor],name="InterestInfoActor")
    val personActor = context.actorOf(Props[PersonActor],name="PersonActor")
    def receive = {
      case id: Int =>
        val combineResult: Future[Person] =
          for {
          //向basicInfoActor发送Send-And-Receive-Future消息，mapTo方法将返回结果映射为BasicInfo类型
            basicInfo <- ask(basicInfoActor, id).mapTo[BasicInfo]
            //向interestInfoActor发送Send-And-Receive-Future消息，mapTo方法将返回结果映射为InterestInfo类型
            interestInfo <- ask(interestInfoActor, id).mapTo[InterestInfo]
          } yield Person(basicInfo, interestInfo)

        //将Future结果发送给PersonActor
        pipe(combineResult).to(personActor)
    }
  }
}

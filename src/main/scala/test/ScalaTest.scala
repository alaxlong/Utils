package test

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging


/**
  * Created by Administrator on 2018/8/13.
  */
class ScalaTest {

}

object ScalaTest{
  def main(args: Array[String]) {
    //创建ActorSystem,ActorSystem为创建和查找Actor的入口
    //ActorSystem管理的Actor共享配置信息如分发器(dispatchers)、部署（deployments）等
    val system = ActorSystem("MessageProcessingSystem")


    //创建ContextActor
    val exampleActor = system.actorOf(Props[ExampleActor],name="ExampleActor")

    //使用fire-and-forget消息模型向exampleActor发送消息
    exampleActor!Run("Running *******")
    exampleActor!Start("Starting **************")

    //关闭ActorSystem
    system.dispatchers
  }
  case class Start(var msg:String)
  case class Run(var msg:String)
  case class Stop(var msg:String)

  class ExampleActor extends Actor {
    val other = context.actorOf(Props[OtherActor], "OtherActor")
    val log = Logging(context.system, this)
    def receive={
      //使用fire-and-forget消息模型向OtherActor发送消息，隐式地传递sender
      case Start(msg) => other ! msg
      //使用fire-and-forget消息模型向OtherActor发送消息，直接调用tell方法，显式指定sender
      case Run(msg) => other.tell(msg, sender)
    }
  }

  class OtherActor extends  Actor{
    val log = Logging(context.system, this)
    def receive ={
      case s:String=>log.info("received message:\n"+s)
      case _      ⇒ log.info("received unknown message")
    }
  }

}
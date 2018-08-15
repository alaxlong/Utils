package sms;
//接口类型：互亿无线触发短信接口，支持发送验证码短信、订单通知短信等。
// 账户注册：请通过该地址开通账户http://sms.ihuyi.com/register.html
// 注意事项：
//（1）调试期间，请用默认的模板进行测试，默认模板详见接口文档；
//（2）请使用APIID（查看APIID请登录用户中心->验证码短信->产品总览->APIID）及 APIkey来调用接口；
//（3）该代码仅供接入互亿无线短信接口参考使用，客户可根据实际需要自行编写；

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import util.StringUtil;


public class sendsms {
	//短信验证码
	//	private static String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	//营销短信
	private static String Url = "http://api.yx.ihuyi.com/webservice/sms.php?method=Submit";

	/**
	 *  互亿无线短信，返回参数为json
	 * @param user APIID
	 * @param pwd APIKEY
	 * @param mobile 接收手机号码， 多个号码请用英文逗号隔开。
	 * @param content 短信内容
	 * @param stime 指定短信下发时间（格式：2016-11-30 15:30:00）
     * @return
     */
	public String huyiSms(String user, String pwd, String mobile, String content, String stime){
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(Url);

		//client.getParams().setContentCharset("GBK");
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

		NameValuePair[] data = {//提交短信
				new NameValuePair("account",user),//查看用户名 登录用户中心->营销短信>产品总览->API接口信息->APIID
				new NameValuePair("password", pwd),//查看密码 登录用户中心->营销短信>产品总览->API接口信息->APIKEY
				new NameValuePair("mobile", mobile),//手机号码，多个号码请用,隔开
				new NameValuePair("content", content),
				new NameValuePair("stime", stime),
				new NameValuePair("format", "json")
		};

		method.setRequestBody(data);
		String SubmitResult = "";
		try {
			client.executeMethod(method);
			SubmitResult = method.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SubmitResult;
	}


	public static void main(String [] args) {

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(Url);

		client.getParams().setContentCharset("GBK");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=GBK");

		int mobile_code = (int)((Math.random()*9+1)*100000);

		String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。");

		NameValuePair[] data = {//提交短信
//				new NameValuePair("account", "C40569832"), //查看用户名是登录用户中心->验证码短信->产品总览->APIID
//				new NameValuePair("password", "ef659a657b7bfd2d0453a482f669774d"),  //查看密码请登录用户中心->验证码短信->产品总览->APIKEY
				new NameValuePair("account", "M76890642"),
				new NameValuePair("password", "305f45fd2d7723d048ff2c87fd6c38f7"),
				//new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
				new NameValuePair("mobile", "18516129614,18662813449"),
//				new NameValuePair("content", content),
				new NameValuePair("content", "尊敬的会员，第五次，推送。退订回TD【Chinapex】"),

		};
		method.setRequestBody(data);

		try {
			client.executeMethod(method);

			String SubmitResult =method.getResponseBodyAsString();

			//System.out.println(SubmitResult);

			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();

			System.out.println(root.getText());
			String code = root.elementText("code");
			String msg = root.elementText("msg");
			String smsid = root.elementText("smsid");

			System.out.println(code);
			System.out.println(msg);
			System.out.println(smsid);

			if("2".equals(code)){
				System.out.println("短信提交成功");
			}

		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
package sms;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: baibing.shang
 * \* Date: 2018/8/15
 * \* Description:
 * \
 */
public class HuYiSmsUtil {
    //短信验证码
    //	private static String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
    //营销短信
    private static String Url = "http://api.yx.ihuyi.com/webservice/sms.php?method=Submit";

    /**
     *  互亿无线短信，返回参数为json
     * @param user APIID 查看用户名 登录用户中心->营销短信>产品总览->API接口信息->APIID
     * @param pwd APIKEY 查看密码 登录用户中心->营销短信>产品总览->API接口信息->APIKEY
     * @param mobile 接收手机号码， 多个号码请用英文逗号隔开。
     * @param content 短信内容
     * @param stime 指定短信下发时间（格式：2016-11-30 15:30:00）
     * @return
     */
    public String sendSms(String user, String pwd, String mobile, String content, String stime){
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(Url);

        client.getParams().setContentCharset("UTF-8");
        method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

        NameValuePair[] data = {//提交短信
                new NameValuePair("account",user),
                new NameValuePair("password", pwd),
                new NameValuePair("mobile", mobile),
                new NameValuePair("content", content),
                new NameValuePair("stime", stime),
                new NameValuePair("format", "json")
        };

        method.setRequestBody(data);
        String submitResult = "";
        try {
            client.executeMethod(method);
            submitResult = method.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return submitResult;
    }
}
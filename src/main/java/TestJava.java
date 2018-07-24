import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elevoc.sdk3rd.taobaoip.TaobaoIP;
import com.elevoc.sdk3rd.taobaoip.TaobaoIPResult;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import util.HttpClientUtil;
import util.IpGeoConst2;
import util.PageUtil;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.json.Json;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: baibing.shang
 * \* Date: 2018/7/18
 * \* Description:
 * \
 */
public class TestJava {
    public static void main(String[] args){
//        System.out.println(PageUtil.getPageStartAndEnd(5));
//        TestJava t = new TestJava();
//        IpGeoConst2.ip2Long("122.144.218.13");
//        String ip= "122.144.218.13";
        String ip= "117.136.42.87";
        /*Map<String,String> prm=new HashMap<String, String>();
        prm.put("ip", ip);
        String  bodynew = HttpClientUtil.doGet("http://ip.taobao.com/service/getIpInfo.php",prm);
        System.out.println(JSON.parseObject(bodynew));*/

        System.out.println(getCityNameBySinaAPI(ip));


    }


    public static String getCityNameBySinaAPI(String ip) {
        String url = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip="
                + ip;
        String cityName = "";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        try {
            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                try {
                    JSONObject jsonResult = JSON.parseObject(strResult);
                    cityName = jsonResult.getString("city");
                    System.out.println(JSON.toJSONString(jsonResult, true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }



}
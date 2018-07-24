import com.alibaba.fastjson.JSON;
import com.elevoc.sdk3rd.taobaoip.TaobaoIP;
import com.elevoc.sdk3rd.taobaoip.TaobaoIPResult;
import util.HttpClientUtil;
import util.IpGeoConst2;

import java.util.HashMap;
import java.util.Map;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: baibing.shang
 * \* Date: 2018/7/20
 * \* Description:
 * \
 */
public class IpResultServer {
    public static void main(String[] args){
        String ip= "117.136.42.86";

        //离线解析
        IpGeoConst2 ipGeoConst2 = new IpGeoConst2();
        ipGeoConst2.geoDataFromMem(ip);

        //ip解析城市
        Map<String,String> prm=new HashMap<String, String>();
        prm.put("ip", ip);
        String  bodynew = HttpClientUtil.doGet("http://ip.taobao.com/service/getIpInfo.php",prm);
        System.out.println(JSON.parseObject(bodynew));

        //调用jar包nutz-1.r.65.jar，TaobaoIP.jar
        TaobaoIPResult result =  TaobaoIP.getResult("69.171.71.32");
        if (result.getCode() == 0){
            System.out.println("国家/地区：" + result.getCountry());
            System.out.println("省份：" + result.getRegion());
            System.out.println("城市：" + result.getCity());
            System.out.println("运营商：" + result.getIsp());
            System.out.println(result.getCounty());
        }else {
            System.err.println("ip地址查询失败，请检查ip地址是否正确");
        }
    }
}
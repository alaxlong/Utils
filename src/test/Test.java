import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * \* Created with IntelliJ IDEA.
 * \* User: baibing.shang
 * \* Date: 2018/7/17
 * \* Description:
 * \
 */
public class Test {
    public static void main(String[] args){
        String json = "{\"data\":{\"feedbacks\":{\"feedbacklist\":[{\"comment\":\"5s\",\"createtime\":\"2016.09.07 12:38\",\"score\":5,\"username\":\"1331##11\"}],\"totalcount\":1,\"totalscore\":5},\"liketeamlist\":[{\"limage\":\"http://baidu.com.465.jpg\",\"lmarketprice\":199,\"lteamId\":386,\"lteamprice\":38,\"ltitle\":\"ss\"},{\"limage\":\"http://baidu.com/37.jpg\",\"lmarketprice\":3380,\"lteamId\":57133,\"lteamprice\":580,\"ltitle\":\"sss\"}],\"partnerteamlist\":[{\"pteamId\":35,\"pteamprice\":228,\"ptitle\":\"ssss\"},{\"pteamId\":72598,\"pteamprice\":2888,\"ptitle\":\"sssss\"},{\"pteamId\":3613,\"pteamprice\":499,\"ptitle\":\"ssssss\"},{\"pteamId\":72638,\"pteamprice\":4299,\"ptitle\":\"aa\"},{\"pteamId\":716,\"pteamprice\":38,\"ptitle\":\"aa\"}]},\"state\":1,\"err\":null}";
//        String json = "{\"name\":\"BeJson\",\"url\":\"http://www.bejson.com\",\"page\":88,\"isNonProfit\":true,\"address\":{\"street\":\"科技园路.\",\"city\":\"江苏苏州\",\"country\":\"中国\"},\"links\":[{\"name\":\"Google\",\"url\":\"http://www.google.com\"},{\"name\":\"Baidu\",\"url\":\"http://www.baidu.com\"},{\"name\":\"SoSo\",\"url\":\"http://www.SoSo.com\"}]}";
        Test t = new Test();
//        Map<String, Object> map = t.jsonToMap(json);
//        System.out.println(map);
        System.out.println("ssss");
}


    /*//json 消息转成Map结构
    public Map<String, Object> jsonToMap(String jsonMapStr) {

        Map<String, Object>  objkeyMap= new HashMap<String, Object>();

        Map<String, Object>  jsonMaps= new HashMap<String, Object>();

        boolean mapJsonbo=true;

        while(mapJsonbo){
            if(jsonMaps.size() <=0 ){
                JSONObject jso = JSONObject.parseObject(jsonMapStr.toString());
                Map<String, Object> jsonMap =  JSONObject.toJavaObject(jso, Map.class);

                jsonMap.forEach((k,v) ->{
                    if(isJson(String.valueOf(v))&& v!=null){
                        jsonMaps.put(k, v);
                    }else{
                        objkeyMap.put(k, v);
                    }
                });
            }else{
                Map<String, Object>  jsonemporaryMap= new HashMap<String, Object>();
                jsonMaps.forEach((k,v) ->{
                    JSONObject jso = JSONObject.parseObject(v.toString());
                    if(jso != null){
                        Map<String, Object> jsonMap =  JSONObject.toJavaObject(jso, Map.class);
                        jsonMap.forEach((lk,lv) ->{
                            if(isJson(String.valueOf(lv))){
                                jsonemporaryMap.put(lk, lv);
                            }else{
                                objkeyMap.put(lk, lv);
                            }
                        });
                    }
                });

                if(jsonemporaryMap.size() <= 0){
                    mapJsonbo=false;
                }
                jsonMaps.clear();
                jsonMaps.putAll(jsonemporaryMap);
            }
        }

        return objkeyMap;
//        objkeyMap.forEach((k,v) ->{
//        	 System.out.println(k+":"+v);
//        } );

    }

    public   boolean isJson(String content){
        try {
            if(content.equals("")){
                return false;
            }
            JSONObject jsonStr= JSONObject.parseObject(content);
            return  true;
        } catch (Exception e) {
            return false;
        }
    }*/


}
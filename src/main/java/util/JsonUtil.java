package util;

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
public class JsonUtil {

    //json 消息转成Map结构
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

    public boolean isJson(String content){
        try {
            if(content.equals("")){
                return false;
            }
            JSONObject jsonStr= JSONObject.parseObject(content);
            return  true;
        } catch (Exception e) {
            return false;
        }
    }
}
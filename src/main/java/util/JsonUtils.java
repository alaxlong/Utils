package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;

import com.google.common.io.Files;
/**
 * \* @Created with IntelliJ IDEA.
 * \* @author: baibing.shang
 * \* @Date: 2018/11/16
 * \* @Description:
 * \
 */
public class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static final TypeReference<Map<String, String>> MAP_TYPE = new TypeReference<Map<String, String>>() {};
    static {
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.getDeserializationConfig().withDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        objectMapper.getSerializationConfig().withDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * 格式化输出，性能有损耗
     */
    public static <T> String toJsonForDisplay(T t) {
        try {
            String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(t);
            return jsonStr;
        } catch (JsonGenerationException e) {
            throw new RuntimeException("JsonGenerationException", e);
        } catch (JsonMappingException e) {
            throw new RuntimeException("JsonMappingException", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException", e);
        }
    }

    public static <T> String toJson(T t) {
        try {
            String jsonStr = objectMapper.writeValueAsString(t);
            return jsonStr;
        } catch (JsonGenerationException e) {
            throw new RuntimeException("JsonGenerationException", e);
        } catch (JsonMappingException e) {
            throw new RuntimeException("JsonMappingException", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException", e);
        }
    }

    public static <T> T fromJSON(String jsonString, Class<T> clazz) {

        T object = null;
        try {
            object = objectMapper.readValue(jsonString, clazz);
        } catch (JsonGenerationException e) {
            throw new RuntimeException("JsonGenerationException", e);
        } catch (JsonMappingException e) {
            throw new RuntimeException("JsonMappingException", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException", e);
        }
        return object;
    }

    public static <T> T fromJSON(String jsonString, TypeReference<T> typeReference) {

        T object = null;
        try {
            object = objectMapper.readValue(jsonString, typeReference);
        } catch (JsonGenerationException e) {
            throw new RuntimeException("JsonGenerationException", e);
        } catch (JsonMappingException e) {
            throw new RuntimeException("JsonMappingException", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException", e);
        }
        return object;
    }

    public static byte[] writeValueAsBytes(Object value) throws IOException{
        return objectMapper.writeValueAsBytes(value);
    }

    public static long readValue(byte[] value) throws IOException{
        return objectMapper.readValue(objectMapper.writeValueAsBytes(value), long.class);
    }

    public static void formatRegion() throws IOException {
        String jsonStr = Files.readFirstLine(new File("district-CN.txt"), Charset.forName("UTF-8"));
        JsonNode chinaProvincesArray = objectMapper.readTree(jsonStr).get("districts").get(0).get("districts");
        Iterator<JsonNode> iterator = chinaProvincesArray.getElements();
        PrintWriter printWriter = new PrintWriter(new FileWriter("origin.txt"));
        while (iterator.hasNext()){
            JsonNode provinceObj = iterator.next();
            String province = provinceObj.get("name").asText();
            Iterator<JsonNode> cityIerator  = provinceObj.get("districts").getElements();
            while (cityIerator.hasNext()){
                JsonNode cityObj = cityIerator.next();
                String city = cityObj.get("name").asText();
                Iterator<JsonNode> districtIerator  = cityObj.get("districts").getElements();
                boolean hasChild = false;
                while (districtIerator.hasNext()){
                    hasChild=true;
                    JsonNode districtObj = districtIerator.next();
                    String district = districtObj.get("name").asText();
                    String location = districtObj.get("center").asText();
                    printWriter.println(String.format("%s;%s;%s;%s", province,city,district,location));
                }
                if(! hasChild){ //香港，澳门，只有两级(台湾只有一级..)
                    printWriter.println(String.format("%s;%s;%s;%s", province,province,city,cityObj.get("center").asText()));
                }
            }
        }
        printWriter.flush();
        printWriter.close();
    }

    public static void formatWeather() throws IOException {
        LineIterator lineIterator = FileUtils.lineIterator(new File("weather.txt"), "UTF-8");
        PrintWriter printWriter = new PrintWriter(new FileWriter("weather-1.txt"));
        while(lineIterator.hasNext()){
            String jsonStr = lineIterator.nextLine();
            JsonNode jsonNode =  objectMapper.readTree(jsonStr);
            String province_district = jsonNode.get("city").asText() + jsonNode.get("area").asText();
            printWriter.println(String.format("%s\001%s", province_district, jsonStr));
        }
        lineIterator.close();
        printWriter.flush();
        printWriter.close();
    }

    public static void main(String[] args) throws JsonProcessingException, IOException {
		String addPrice = "{\"add_price_redispatch\":0,\"add_price_rate\":0,\"add_price_max_amount\":10000000,\"add_price_type\":0,\"add_price_vip\":0,\"strategy_id\":0,\"add_total_amount\":0,\"add_amount_str\":\"\",\"add_amount_str_full\":\"\"}";
		Map<String, String> t1 = JsonUtils.fromJSON(addPrice, JsonUtils.MAP_TYPE);
		System.out.println(t1);
		System.out.println(t1.get("add_price_type"));


		//嵌套json
		String jsonStr = "{'queryLocation':[23.9386500000,115.7825750000],'addrList':[{'type':'poi','status':1,'name':'犁滩村','id':'ANB02F102VX0','admCode':'441424','admName':'广东省,梅州市,五华县,','addr':'','nearestPoint':[115.77923,23.93969],'distance':349.817}]}";
		String admName = objectMapper.readTree(jsonStr).get("addrList").get(0).get("admName").asText();
		System.err.println("admName = " + admName);

//        formatWeather();

    }
}
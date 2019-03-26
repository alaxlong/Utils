import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: baibing.shang
 * \* Date: 2018/9/6
 * \* Description:
 * \
 */
public class Es {

    private Logger logger = LoggerFactory.getLogger(Es.class);
    private static TransportClient client;
    public final static String [] HOST = {"192.168.208.51", "192.168.208.44", "192.168.208.45", "192.168.208.43"};
    public final static String CLUSTNAME = "dszcj";
    public final static int PORT = 9300;//http请求的端口是9200，客户端是9300

    private static String index = "shangtest";   // 要操作的索引库为"shangtest"
    private static String type = "baibing";    // 要操作的类型为"t1"

    public static TransportClient getTransportClient() throws UnknownHostException{

        // 集群设置
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")//集群名称
                .build();
        TransportClient client = new PreBuiltTransportClient(settings);
        //添加集群地址和tcp服务端口 IP是由集群的各个node的ip组成的数组
        try {
            for (String ip : HOST) {
//                client.addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName(ip), 9300));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    public static void main(String[] args){
        try {
            TransportClient transportClient = getTransportClient();
            CreateIndexRequestBuilder createIndex=transportClient.admin().indices().prepareCreate(index);
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties") //设置之定义字段
                    .startObject("name").field("type","text").field("analyzed","standard").endObject() //设置分析器
                    .startObject("age").field("type","long").endObject()
                    .startObject("class_name").field("type","keyword").endObject()
                    .startObject("birth").field("type","date").field("format","yyyy-MM-dd").endObject()//设置Date的格式
                    .endObject()
                    .endObject();
            createIndex.addMapping(type, mapping);
            CreateIndexResponse res=createIndex.execute().actionGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
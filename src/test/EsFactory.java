import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: baibing.shang
 * \* Date: 2018/9/12
 * \* Description:
 * \
 */

public class EsFactory {
    private static Map<String, Client> CLIENTPOOLS;

    private static String clusterName;
    private static String clusterHosts;

    private static final int DEFAULT_PORT = 9300;

    //初始化数据，此步可被替换为Spring注入
    static {
        clusterName = "dszcj";
        clusterHosts = "192.168.208.51";
        init();
    }

    private static void init() {
        initPools();
        initClients();
    }

    private static void initPools() {
        if (null == CLIENTPOOLS) {
            CLIENTPOOLS = new HashMap<>();
        }
    }

    public static void initClients() {
        Client client = builder().setClusterName(clusterName).addNode(clusterHosts).build();
        CLIENTPOOLS.put(clusterName, client);
    }

    public static EsFactory.Builder builder(){
        return new Builder();
    }

    public static Client getEsClient(String clusterName) {
        return CLIENTPOOLS.get(clusterName);
    }


    private static class Builder {
        private TransportClient _client;

        public EsFactory.Builder setClusterName(String clusterName) {
            this._client = new PreBuiltTransportClient(getSettings(clusterName));
            return this;
        }

        private Settings getSettings(String clusterName) {
            return Settings.builder()
                    .put("cluster.name", clusterName)
                    .put("client.transport.ignore_cluster_name", false)
                    .put("client.transport.sniff", true)
                    .build();
        }

        public EsFactory.Builder addNode(String nodeAddrs) {
            String[] hosts = nodeAddrs.split(",");
            for (String host : hosts) {
                String infos[] = host.split(":");
                addNode(infos[0], infos.length == 2 ? Integer.valueOf(infos[1]) : DEFAULT_PORT);
            }
            return this;
        }

        public EsFactory.Builder addNode(String ip, int port) {
            try {
//                this._client.addTransportAddress(
//                        new InetSocketTransportAddress(InetAddress.getByName(ip), port));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public TransportClient build() {
            return this._client;
        }
    }

    public static void main(String[] args){
        NodesInfoResponse resp =getEsClient(clusterName).admin().cluster().prepareNodesInfo().execute().actionGet();
        Iterator<NodeInfo> nodes =resp.getNodes().iterator();
        while (nodes.hasNext()) {
            DiscoveryNode node = nodes.next().getNode();
            System.out.println("version:" + node.getVersion());
            System.out.println("address:" + node.getHostAddress());
            System.out.println("nodeId:" + node.getId());
            System.out.println("nodeInfo:" + JSON.toJSONString(node));
        }



    }
}

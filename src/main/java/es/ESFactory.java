package es;

import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.http.JestHttpClient;

/**
 * \* @Created with IntelliJ IDEA.
 * \* @author: baibing.shang
 * \* @Date: 2018/11/16
 * \* @Description:
 * \
 */
public class ESFactory {
    private static JestHttpClient client;

    private ESFactory() {

    }

    public synchronized static JestHttpClient getClient() {
        if (client == null) {
            JestClientFactory factory = new JestClientFactory();
            factory.setHttpClientConfig(new HttpClientConfig.Builder(
                    "http://139.219.12.113:9200").multiThreaded(true).build());
            client = (JestHttpClient) factory.getObject();
        }
        return client;
    }

    public static void main(String[] args) {
        JestHttpClient client = ESFactory.getClient();
        System.out.println(client.getAsyncClient());
//        System.out.println(client.getServers());
        client.shutdownClient();
    }
}
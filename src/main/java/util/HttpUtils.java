package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
/**
 * \* @Created with IntelliJ IDEA.
 * \* @author: baibing.shang
 * \* @Date: 2018/11/16
 * \* @Description:
 * \
 */
public class HttpUtils {
    public static String httpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse;
        String result = "";
        try {
            httpResponse = HttpPool.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                result = EntityUtils.toString(httpEntity, "UTF-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String httpPost(String url, byte[] body) {
        HttpResponse httpResponse = null;
        HttpPost httpPost = new HttpPost(url);
        String result = null;
        try {
            ByteArrayEntity entity = new ByteArrayEntity(body);
            httpPost.setEntity(entity);
            httpResponse = HttpPool.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                result = EntityUtils.toString(httpEntity, "UTF-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static HttpResponse httpPost(String url, Map<String, String> paramMap) {
        HttpResponse httpResponse = null;
        HttpPost httpPost = new HttpPost(url);
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Entry<String, String> param : paramMap.entrySet()) {
                params.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(entity);
            httpResponse = HttpPool.execute(httpPost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpResponse;
    }

    public static String getStringPost(String url, Map<String, String> paramMap) {
        HttpResponse httpResponse = null;
        HttpPost httpPost = new HttpPost(url);
        String result = null;
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Entry<String, String> param : paramMap.entrySet()) {
                params.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(entity);
            httpResponse = HttpPool.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                result = EntityUtils.toString(httpEntity, "UTF-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String jsonPost(String url, String body) {
        HttpResponse httpResponse = null;
        HttpPost httpPost = new HttpPost(url);
        String result = "";
        try {
            StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            httpResponse = HttpPool.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                result = EntityUtils.toString(httpEntity, "UTF-8");
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    private static enum HttpPool {
        INSTANCE;
        /**
         * 连接池里的最大连接数
         */
        private static final int MAX_TOTAL_CONNECTIONS = 5;
        /**
         * 每个路由的默认最大连接数
         */
        private static final int MAX_ROUTE_CONNECTIONS = 50;
        /**
         * 连接超时时间
         */
        private static final int CONNECT_TIMEOUT = 10000;
        /**
         * 套接字超时时间
         */
        private static final int SO_TIMEOUT = 10000;
        /**
         * 连接池中 连接请求执行被阻塞的超时时间
         */
        private static final long CONN_MANAGER_TIMEOUT = 10000;

        private static HttpClient httpClient;

        static {
            // 设置组件参数, HTTP协议的版本,1.1/1.0/0.9
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
            HttpProtocolParams.setUseExpectContinue(params, true);

            params.setParameter(ClientPNames.CONN_MANAGER_TIMEOUT, CONN_MANAGER_TIMEOUT);
            params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);
            params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);

            // 设置访问协议
            SchemeRegistry schreg = new SchemeRegistry();
            schreg.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
            schreg.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

            // 多连接的线程安全的管理器
            PoolingClientConnectionManager pccm = new PoolingClientConnectionManager(schreg);
            pccm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS); // 每个路由的最大并行链接数
            pccm.setMaxTotal(MAX_TOTAL_CONNECTIONS); // 客户端总并行链接最大数
            pccm.setDefaultMaxPerRoute(2);
//			org.apache.http.HttpHost localhost = new org.apache.http.HttpHost("locahost", 80);
//			pccm.setMaxPerRoute(new HttpRoute(localhost), 80);//对本机80端口的socket连接上限是80
            httpClient = new DefaultHttpClient(pccm, params);
        }

        static HttpResponse execute(HttpUriRequest request) throws ClientProtocolException, IOException {
            return httpClient.execute(request);
        }
    }
}
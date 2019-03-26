/**
 * \* Created with IntelliJ IDEA.
 * \* User: baibing.shang
 * \* Date: 2018/9/6
 * \* Description:
 * \
 */

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchTest1 {
    private Logger logger = LoggerFactory.getLogger(ElasticsearchTest1.class);

    private static TransportClient client;
    //    public final static String HOST = "139.219.12.113";
    public final static String HOST = "192.168.208.51";
    public final static String CLUSTNAME = "dszcj";
    public final static int PORT = 9300;//http请求的端口是9200，客户端是9300

    private static String index = "my";   // 要操作的索引库为"shangtest"
    private static String type = "test";    // 要操作的类型为"t1"

    public TransportClient getClient() {
        try {
            //创建客户端
//            client = new PreBuiltTransportClient(getSettings(CLUSTNAME)).addTransportAddresses(
//                    new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    private Settings getSettings(String clusterName) {
        return Settings.builder()
                .put("cluster.name", clusterName)
                .put("client.transport.ignore_cluster_name", false)
                .put("client.transport.sniff", true)
                .build();
    }

    public void closeConnect() {
        if (null != client) {
            logger.info("执行关闭连接操作...");
            client.close();
        }
    }

    /**
     * String json input to Es
     *
     * @param json
     */
    public void jsonToEs(String index, String type, String json) {
        IndexRequestBuilder create2 = client.prepareIndex(index, type)
                .setSource(json, XContentType.JSON);
        IndexResponse response2 = create2.execute().actionGet();
    }

    /**
     * map to Es
     *
     * @param map
     */
    public void mapToEs(String index, String type, Map<String, Object> map) {
        IndexRequestBuilder create = client.prepareIndex(index, type)
                .setSource(map);
        IndexResponse response = create.execute().actionGet();
    }

    /**
     * list<Map> to Es
     * 批量插入
     *
     * @param sources
     * @throws UnknownHostException
     */
    public void bulkInsert(String index, String type, List<Map<String, Object>> sources) throws UnknownHostException {

        BulkRequestBuilder bulkRequest = getClient().prepareBulk();
        //sources Map Array
        for (Map source : sources) {
            //添加请求
            bulkRequest.add(getClient().prepareIndex(index, type)
                    .setSource(source));
        }
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
    }

    /**
     * 获取ES集群各个节点信息
     */
    public void nodeMessage() {
        NodesInfoResponse resp = getClient().admin().cluster().prepareNodesInfo().execute().actionGet();
        Iterator<NodeInfo> nodes = resp.getNodes().iterator();
        while (nodes.hasNext()) {
            DiscoveryNode node = nodes.next().getNode();
            System.out.println("version:" + node.getVersion());
            System.out.println("address:" + node.getHostAddress());
            System.out.println("nodeId:" + node.getId());
            System.out.println("nodeInfo:" + JSON.toJSONString(node));
        }
    }

    /**
     * 查询所有索引
     */
    public void getIndex() {
        GetIndexResponse resp = getClient().admin().indices().prepareGetIndex().execute().actionGet();
        for (String index : resp.getIndices()) {
            System.out.println(index);
        }
    }

    /**
     * 获取指定索引的存储信息
     */
    public void indexMessage(String index) {
        IndicesStatsResponse resp = getClient().admin().indices().prepareStats()
                .setIndices(index)
                .execute().actionGet();
        System.out.println(resp);
    }

    /**
     * 删除索引
     *
     * @throws UnknownHostException
     */
    public boolean deleteIndex(String index) throws UnknownHostException {
        //先判断下索引是否存在
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);
        IndicesExistsResponse inExistsResponse = getClient().admin().indices()
                .exists(inExistsRequest).actionGet();
        if (!inExistsResponse.isExists()) {
            return false;
        }
        //存在即进行删除操作
        DeleteIndexResponse dResponse = null;
        if (inExistsResponse.isExists()) {
            DeleteIndexRequestBuilder delete = getClient().admin().indices().prepareDelete(index);
            dResponse = delete.execute().actionGet();

        }
        return dResponse.isAcknowledged();
    }

    /**
     * 通过Id删除一条记录
     *
     * @param index
     * @param type
     * @param id
     */
    public void deleteById(String index, String type, String id) {
        DeleteRequestBuilder delete = client.prepareDelete(index, type, id);
        DeleteResponse response = delete.execute().actionGet();
        //TODO 测试 添加返回值

    }

    /**
     * 根据搜索返回值删除数据
     *
     * @param query
     */
    public void deleteByQuery(QueryBuilder query) {
        DeleteByQueryAction.INSTANCE.newRequestBuilder(getClient()).filter(query).execute();
        //TODO 测试 和 返回值
    }


    /**
     * 构建查询条件QueryBuilder查询数据
     *
     * @param index
     * @param type
     * @param field
     * @param text
     */
    public void searchByQuery(String index, String type, String field, String text) {
        //构建一个query 即查询条件
        //字段值包含搜索
        QueryBuilder match = QueryBuilders.matchQuery(field, text);

        //字段值精确值匹配
//        QueryBuilder term = QueryBuilders.termQuery("field","text");
//        QueryBuilder terms = QueryBuilders.termsQuery("field","text","text2","text3");

        //前缀搜索
//        QueryBuilder prefix= QueryBuilders.prefixQuery("field","text");

        //模糊值搜索
//        FuzzyQueryBuilder fuzzy= QueryBuilders.fuzzyQuery(field, text);

        //通配符搜索
//        QueryBuilder wildcard= QueryBuilders.wildcardQuery(field, patten);

        //搜索语句搜索
//        QueryBuilder queryString = QueryBuilders.queryStringQuery("queryString");


        //根据查询条件构建一个查询问句
        SearchRequestBuilder search = getClient().prepareSearch(index)
                .setQuery(match)
                .setTypes(type)  //指定类型 可选
                .setFrom(0).setSize(10) //分页 可选
                .addSort("title", SortOrder.DESC);//排序 可选
        //搜索返回搜索结果
        SearchResponse response = search.get();
        //命中的文档
        SearchHits hits = response.getHits();
        //命中总数
        Long total = hits.getTotalHits();
        //循环查看命中值
        for (SearchHit hit : hits.getHits()) {
            //文档元数据
            String ind = hit.getIndex();
            System.out.println(ind);
            //文档的_source的值
            Map<String, Object> sourceMap = hit.getSourceAsMap();
        }
    }


    /**
     * 查询索引所有数据
     */
    public void searchAll(String index, String type) {
        QueryBuilder matchAll = QueryBuilders.matchAllQuery();

        //根据查询条件构建一个查询问句
        SearchRequestBuilder search = getClient().prepareSearch(index)
                .setQuery(matchAll)
                .setTypes(type)  //指定类型 可选
                .setFrom(0).setSize(10) //分页 可选
                .addSort("title", SortOrder.DESC);//排序 可选
        //搜索返回搜索结果
        SearchResponse response = search.get();
        //命中的文档
        SearchHits hits = response.getHits();
        //命中总数
        Long total = hits.getTotalHits();
        //循环查看命中值
        for (SearchHit hit : hits.getHits()) {
            //文档元数据
            String ind = hit.getIndex();
            System.out.println(ind);
            //文档的_source的值
            Map<String, Object> sourceMap = hit.getSourceAsMap();
        }
    }

    /**
     * 多条件搜索
     */
    public void boolQuery() {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.should(QueryBuilders.matchQuery("main_body", "Elasticsearch JAVA")
                .operator(Operator.AND).boost(1))
                .should(QueryBuilders.matchQuery("title", "Elasticsearch JAVA")
                        .operator(Operator.OR).boost(10));
        //TODO 未完成
    }

    /**
     * 聚合搜索
     * @param index
     * @param aggName 聚合内容别名
     * @param field 聚合字段
     */
    public void aggreSearch(String index, String aggName, String field) {

        //需要给聚合内容一个别名,聚合某字段
        AggregationBuilder aggregation = AggregationBuilders
                .terms(aggName).field(field);

        //统计某字段的总数
//        ValueCountAggregationBuilder aggregation =  AggregationBuilders.count("alias").field("field");

        //去重统计某字段的总数
//        CardinalityAggregationBuilder aggregation = AggregationBuilders.cardinality("alias").field("field");

        //聚合时间
        /*AggregationBuilder aggregation = AggregationBuilders
                .dateHistogram(aggName).field(field)
                .format("yyyy-MM-dd")
                .dateHistogramInterval(DateHistogramInterval.DAY);*/

        QueryBuilder allQuery = QueryBuilders.matchAllQuery();
        SearchResponse response = getClient().prepareSearch(index)
                .setQuery(allQuery).addAggregation(aggregation).get();
        //根据别名获取聚合对象，不同聚合会返回不同的聚合对象
        Terms terms = response.getAggregations().get(aggName);
        for (Terms.Bucket entry : terms.getBuckets()) {
            //聚合的属性值
            String value = entry.getKey().toString();
            //聚合后的数量
            long count = entry.getDocCount();
        }
    }

    /**
     * 搜索结果高亮显示
     * @param index 索引
     * @param filed 要查的字段
     * @param value 字段的值
     */
    public void highLgtSearch(String index, String filed, String value) {
        QueryBuilder query = QueryBuilders.matchQuery(filed, value);

        //创建高亮配置
        HighlightBuilder hiBuilder=new HighlightBuilder();
        //配置高亮作用字段
        hiBuilder.field(filed);
        //可以设置返回高亮词周围多少字符，以及返回几个段
        hiBuilder.field("main_body",100,1);
        //在高亮词前添加标签
        hiBuilder.preTags("<em style='color:red'>");
        //高亮词的后标签
        hiBuilder.postTags("</em>");

        SearchResponse response = getClient().prepareSearch(index)
                .setQuery(query).highlighter(hiBuilder).get();
        //循环命中
        for(SearchHit hit:response.getHits().getHits()){
            //比如配置了两个字段都高亮，key是字段名，value就是加了高亮的值
            Map<String, HighlightField> highlightFieldMap = hit.getHighlightFields();
            HighlightField highlightField = highlightFieldMap.get(filed);
            if(highlightField!=null){
                //获取高亮的所有段
                Text[] fragments = highlightField.getFragments();
                for(Text text:fragments){
                    System.out.println("text的值是："+text);
                }
            }
        }
    }


    public static void main(String[] args) {
        ElasticsearchTest1 es = new ElasticsearchTest1();
        es.nodeMessage();
//        es.getIndex();
        /*try {
            System.out.println(es.deleteIndex("my"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }*/

        Map<String, Object> data = new HashMap<>();
        data.put("t1", "qqqqqq");
        data.put("t2", "wwwwww");


    }
}
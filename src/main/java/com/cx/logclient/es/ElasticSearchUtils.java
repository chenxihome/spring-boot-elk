package com.cx.logclient.es;

import com.cx.logclient.util.JsonUtil;
import com.cx.logclient.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * Created by cx on 2017/12/1.
 */
@Slf4j
@Component
public class ElasticSearchUtils {
    private final static String COMMA = ",";
    private static PreBuiltXPackTransportClient client=null;
    private final static String ipListStr = "xx.xx.xx.xx,xx.xx.xx.xx";

    /**
     * 获取es 客户端
     *
     * @return 客户端实例
     */
    static {
            try {
                Settings settings = Settings.builder()
                        .put("cluster.name", "jrdeves")
                        .put("client.transport.sniff", true)
//                                    .put("xpack.security.transport.ssl.enabled", false)
//                                    .put("xpack.security.user","elastic:changeme")
                        .build();
                System.setProperty("es.set.netty.runtime.available.processors", "false");
                client = new PreBuiltXPackTransportClient(settings);
                for (String nodes : ipListStr.split(COMMA)) {
                    String[] inetSocket = nodes.split(":");
                    String address = inetSocket[0];
                    Integer port = Integer.valueOf(inetSocket[1]);
                    client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(address), port));
                }
            } catch (UnknownHostException e) {
                log.error("初始化客户端ElasticSearch client异常:", e);
            }
        }

    private ElasticSearchUtils() {
    }
    public static BulkProcessor getBulkProcessor() throws Exception {
        BulkProcessor bulkProcessor = BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {
                        log.info("---尝试插入" + request.numberOfActions() + "条数据---");
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request, BulkResponse response) {
                        log.info("---尝试插入" + request.numberOfActions() + "条数据成功---");

                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {
                        log.error("[es错误]---尝试插入数据失败---", failure);

                    }
                })
                .setBulkActions(100)
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .build();
        return bulkProcessor;
    }

    public static boolean saveDocument(String indexName, String typeName, Map<String, Object> jsonMap) throws Exception {
        IndexResponse response;
        try {
            response = client.prepareIndex(indexName, typeName).setSource(jsonMap).get();
        } catch (Exception e) {
            log.warn("es保存数据异常,发起重试objectMap:" + JsonUtil.convertToJson(jsonMap));
            //异常发起重试
            Thread.sleep(200);
            response = client.prepareIndex(indexName, typeName).setSource(jsonMap).get();
        }
        return StringUtil.isEmpty(response.getId()) ? false : true;
    }

    /**
     * 判断指定的索引的类型是否存在
     *
     * @param indexName 索引名
     * @return 存在：true; 不存在：false;
     */
    public static boolean isExistsType(String indexName) throws Exception {
        IndicesExistsRequest request = new IndicesExistsRequest(indexName);
        IndicesExistsResponse response = client.admin().indices().exists(request).actionGet();
        return response.isExists();

    }

    /**
     * 删除指定索引
     *
     * @param indexName 索引名
     * @return
     */
    public static boolean deleteIndexByName(String indexName) throws Exception {
        DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName)
                .execute().actionGet();
        return dResponse.isAcknowledged();
    }

    /**
     * 判断指定的索引的类型是否存在
     *
     * @param indexName 索引名
     * @param indexType 索引类型
     * @return 存在：true; 不存在：false;
     */
    public static boolean deleteDoceMentById(String indexName, String indexType, String id) throws Exception {
        client.prepareDelete(indexName, indexType, id).execute().actionGet();
        return true;
    }

    public static boolean createMapping(String indexName, String typeName, XContentBuilder mappping) throws Exception {
        PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(typeName).source(mappping);
        return client.admin().indices().putMapping(mapping).actionGet().isAcknowledged();
    }

    public static boolean createIndex(String indexName) throws Exception {
        CreateIndexResponse indexResponse = client.admin().indices().prepareCreate(indexName).setSettings(SettingUtils.getSetting(indexName), XContentType.JSON).get();
        return indexResponse.isAcknowledged();
    }

    public static SearchHits queryDocumetByCondition(String indexName, String typeName, Integer pageIndex, Integer pageSize, QueryBuilder queryBuilder, QueryBuilder fiterBuilder, List<SortBuilder> sortBuilders, Float minScore) throws Exception {
        SearchRequestBuilder srb = client.prepareSearch(indexName);
        srb.setSearchType(SearchType.QUERY_THEN_FETCH);
        srb.setTypes(typeName);
        srb.setQuery(queryBuilder);
        //指定最小分数
        if (minScore != null && minScore > 0) {
            srb.setMinScore(minScore);
        }
        if (fiterBuilder != null) {
            srb.setPostFilter(fiterBuilder);
        }
        //按条件排序
        if (!CollectionUtils.isEmpty(sortBuilders)) {
            for (SortBuilder sortBuilder : sortBuilders) {
                srb.addSort(sortBuilder);
            }
        }
        srb.setFrom((pageIndex - 1) * pageSize).setSize(pageSize)
                .setExplain(true);
        SearchResponse response = srb.execute().actionGet();
        return response.getHits();
    }
    public static SearchHits queryDocumetByHighlightCondition(String indexName, String typeName, Integer pageIndex, Integer pageSize, QueryBuilder queryBuilder, HighlightBuilder highlightBuilder, List<SortBuilder> sortBuilders) throws Exception {
        SearchRequestBuilder srb = client.prepareSearch(indexName);
        srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        srb.setTypes(typeName);
        srb.setQuery(queryBuilder);
        srb.highlighter(highlightBuilder);
        //按评分高低排序
        srb.addSort(SortBuilders.scoreSort()
                .order(SortOrder.DESC));
        if (!CollectionUtils.isEmpty(sortBuilders)) {
            for (SortBuilder sortBuilder : sortBuilders) {
                srb.addSort(sortBuilder);
            }
        }
        srb.setFrom((pageIndex - 1) * pageSize).setSize(pageSize)
                .setExplain(true);

        SearchResponse response = srb.execute().actionGet();
        return response.getHits();
    }
}


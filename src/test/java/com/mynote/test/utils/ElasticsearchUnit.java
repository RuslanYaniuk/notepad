package com.mynote.test.utils;

import com.jayway.jsonpath.JsonPath;
import com.mynote.models.User;
import net.minidev.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.client.Requests.refreshRequest;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Component
public class ElasticsearchUnit {

    public static final String TYPE_NAME = "note";
    public static final String INDEX_NAME_PREFIX = "mynote_";
    public static final String NOTES_FIXTURES = "elasticsearch-datasets/notes.bulk";

    private static Map<String, List<JSONObject>> CACHE = new HashMap<>();

    @Autowired
    private Client client;

    public static String getBulkData() throws IOException {
        return IOUtils.toString(new ClassPathResource(NOTES_FIXTURES).getInputStream());
    }

    public void cleanInsertNotes(User user) throws IOException, ExecutionException, InterruptedException {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        String indexName = INDEX_NAME_PREFIX + user.getUsername();
        List<JSONObject> userNotes;

        dropIndex(indexName);
        createIndex(indexName);

        if ((userNotes = CACHE.get(indexName)) == null) {
            userNotes = JsonPath.read(getBulkData(), "$.['" + indexName + "']");
            CACHE.put(indexName, userNotes);
        }
        for (JSONObject noteFixture : userNotes) {
            bulkRequest.add(
                    client.prepareIndex(indexName, TYPE_NAME, extractId(noteFixture))
                            .setSource(prepareBulkRequest(noteFixture)));
        }

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            throw new IllegalArgumentException("Could not insert fixtures");
        }
        refresh(indexName);
    }

    private String prepareBulkRequest(JSONObject input) {
        input = (JSONObject) input.clone();
        input.replace("id", null);
        return input.toString();
    }

    private String extractId(JSONObject input) {
        return JsonPath.read(input, "$.id");
    }

    private void createIndex(String indexName) throws ExecutionException, InterruptedException {
        if (isExists(indexName)) {
            return;
        }
        client.
                admin().
                indices().
                prepareCreate(indexName).
                execute().
                actionGet().
                isAcknowledged();
    }

    private void dropIndex(String indexName) throws ExecutionException, InterruptedException {
        if (!isExists(indexName)) {
            return;
        }
        client.
                prepareDeleteByQuery(indexName).
                setTypes(TYPE_NAME).
                setQuery(QueryBuilders.matchAllQuery()).
                execute().
                actionGet();
    }

    private boolean isExists(String indexName) throws InterruptedException, ExecutionException {
        return client.
                admin().
                indices().
                exists(new IndicesExistsRequest(indexName)).
                get().
                isExists();
    }

    private void refresh(String indexName) {
        client.
                admin().
                indices().
                refresh(refreshRequest(indexName).force(true)).
                actionGet();
    }
}

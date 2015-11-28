package com.mynote.test.utils;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import static com.mynote.test.conf.TestElasticSearchConfig.INDEX_NAME;
import static org.elasticsearch.client.Requests.refreshRequest;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Component
public class ElasticSearchUnit {

    public static final String TYPE_NAME = "note";
    public static final String NOTES_FIXTURES = "elasticsearch-datasets/notes.bulk";

    private static String BULK_DATA;

    @Autowired
    private Client client;

    public void loadFixtures() throws IOException, ExecutionException {
        StringTokenizer st = new StringTokenizer(getBulkData(), "\n");
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        removeAllFixtures();

        while (st.hasMoreTokens()) {
            String data = st.nextToken();
            StringTokenizer dataTokenizer = new StringTokenizer(data, ";");

            if (dataTokenizer.countTokens() == 2) {
                String pk = dataTokenizer.nextToken();
                String content = dataTokenizer.nextToken();
                bulkRequest.add(client.prepareIndex(INDEX_NAME, TYPE_NAME, pk)
                        .setSource(content));
            }
        }
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            throw new IllegalArgumentException("Could not insert fixtures");
        }
        refresh();
    }

    public static String getBulkData() throws IOException {
        if (BULK_DATA == null) {
            BULK_DATA = IOUtils.toString(new ClassPathResource(NOTES_FIXTURES).getInputStream());
        }
        return BULK_DATA;
    }

    private void removeAllFixtures() throws ExecutionException {
        client.prepareDeleteByQuery(INDEX_NAME)
                .setQuery(QueryBuilders.matchAllQuery())
                .setTypes(TYPE_NAME)
                .get();
        refresh();
    }

    private void refresh() {
        client.admin().indices().refresh(refreshRequest(INDEX_NAME).force(true)).actionGet();
    }
}

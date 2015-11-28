package com.mynote.test.conf;

import com.mynote.config.persistence.ElasticSearchConfig;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class TestElasticSearchConfig extends ElasticSearchConfig {

    public static final String INDEX_NAME = "mynote";

    @Autowired
    private EmbeddedElasticsearchServer embeddedElasticsearchServer;

    @Bean
    public EmbeddedElasticsearchServer embeddedElasticsearchServer() {
        return new EmbeddedElasticsearchServer();
    }

    @Override
    public Client client() {
        return embeddedElasticsearchServer.getClient();
    }

    public class EmbeddedElasticsearchServer {

        private static final String DEFAULT_DATA_DIRECTORY = "target/elasticsearch-data";

        private final Node node;
        private final String dataDirectory;

        public EmbeddedElasticsearchServer() {
            this(DEFAULT_DATA_DIRECTORY);
        }

        public EmbeddedElasticsearchServer(String dataDirectory) {
            this.dataDirectory = dataDirectory;

            ImmutableSettings.Builder elasticsearchSettings = ImmutableSettings.settingsBuilder()
                    .put("http.enabled", "false")
                    .put("path.data", dataDirectory);

            node = nodeBuilder()
                    .local(true)
                    .settings(elasticsearchSettings.build())
                    .node();
        }

        public Client getClient() {
            return node.client();
        }

        public void shutdown() {
            node.close();
            deleteDataDirectory();
        }

        private void deleteDataDirectory() {
            try {
                FileUtils.deleteDirectory(new File(dataDirectory));
            } catch (IOException e) {
                throw new RuntimeException("Could not delete data directory of embedded elasticsearch server", e);
            }
        }
    }
}

package com.mynote.config.persistence;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.mynote.repositories.elastic")
public class ElasticSearchConfig {

    @Bean
    public Client client() {
        TransportAddress address = new InetSocketTransportAddress("localhost", 9300);
        TransportClient client = new TransportClient();

        client.addTransportAddress(address);

        return client;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(client());
    }
}

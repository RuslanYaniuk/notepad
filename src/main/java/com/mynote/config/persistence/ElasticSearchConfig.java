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

import javax.servlet.ServletContext;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.mynote.repositories.elastic")
public class ElasticSearchConfig {

    public static final String ELASTICSEARCH_HOST_PARAM_NAME = "mynote.elasticsearch.host";
    public static final String ELASTICSEARCH_PORT_PARAM_NAME = "mynote.elasticsearch.port";

    @Bean
    public Client client(ServletContext servletContext) {
        String host = servletContext.getInitParameter(ELASTICSEARCH_HOST_PARAM_NAME);
        String port = servletContext.getInitParameter(ELASTICSEARCH_PORT_PARAM_NAME);
        TransportAddress address = new InetSocketTransportAddress(host, Integer.parseInt(port));
        TransportClient client = new TransportClient();

        client.addTransportAddress(address);

        return client;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate(Client client) {
        return new ElasticsearchTemplate(client);
    }
}

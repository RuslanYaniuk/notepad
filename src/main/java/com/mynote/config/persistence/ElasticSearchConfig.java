package com.mynote.config.persistence;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private Client client;

    @Bean
    public Client client() {
        String host = servletContext.getInitParameter("mynote.elasticsearch.host");
        String port = servletContext.getInitParameter("mynote.elasticsearch.port");
        TransportAddress address = new InetSocketTransportAddress(host, Integer.parseInt(port));
        TransportClient client = new TransportClient();

        client.addTransportAddress(address);

        return client;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(client);
    }
}

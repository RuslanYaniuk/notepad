package com.mynote.config.persistence;

import com.mynote.repositories.elasticsearch.NoteRepository;
import com.mynote.utils.elasticsearch.CustomElasticsearchRepositoryFactory;
import com.mynote.utils.elasticsearch.CustomEntityMapper;
import com.mynote.utils.elasticsearch.RepositoryProxy;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;

import javax.servlet.ServletContext;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Configuration
@EnableAspectJAutoProxy
public class ElasticsearchConfig {

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
    public ElasticsearchOperations elasticsearchTemplate(Client client, EntityMapper customEntityMapper) {
        ElasticsearchConverter converter = new MappingElasticsearchConverter(new SimpleElasticsearchMappingContext());
        ResultsMapper mapper = new DefaultResultMapper(converter.getMappingContext(), customEntityMapper);

        return new ElasticsearchTemplate(client, converter, mapper);
    }

    @Bean
    public CustomElasticsearchRepositoryFactory elasticsearchRepositoryFactory(ElasticsearchOperations elasticsearchTemplate) {
        return new CustomElasticsearchRepositoryFactory(elasticsearchTemplate);
    }

    @Bean
    public EntityMapper customEntityMapper() {
        return new CustomEntityMapper();
    }

    @Bean
    public NoteRepository noteRepositoryImpl(CustomElasticsearchRepositoryFactory elasticsearchRepositoryFactory) {
        return elasticsearchRepositoryFactory.getNoteRepository();
    }

    @Bean
    public RepositoryProxy noteRepositoryProxy() {
        return new RepositoryProxy();
    }
}

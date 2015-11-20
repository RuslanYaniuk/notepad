package com.mynote.config.persistence;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.servlet.ServletContext;

import static com.mynote.config.web.Constants.PROPERTY_NAME_MONGO_URI;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.mynote.repositories.mongo")
public class MongoConfig extends AbstractMongoConfiguration {

    @Autowired
    private ServletContext servletContext;

    @Override
    protected String getDatabaseName() {
        return "mynote";
    }

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        String mongoURI = servletContext.getInitParameter(PROPERTY_NAME_MONGO_URI);
        MongoClientURI mongoClientURI = new MongoClientURI(mongoURI);

        return new MongoClient(mongoClientURI);
    }

    @Override
    protected String getMappingBasePackage() {
        return "com.mynote.models";
    }
}

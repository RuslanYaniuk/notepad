package com.mynote.test.commons;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mynote.config.persistence.MongoConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

import static com.mynote.config.web.Constants.PROPERTY_NAME_MONGO_URI;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class TestMongoConfig extends MongoConfig {

    @Resource
    private Environment env;

    @Override
    protected String getDatabaseName() {
        return "mynote_test";
    }

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        String mongoURI = env.getRequiredProperty(PROPERTY_NAME_MONGO_URI);
        MongoClientURI mongoClientURI = new MongoClientURI(mongoURI);

        return new MongoClient(mongoClientURI);
    }
}

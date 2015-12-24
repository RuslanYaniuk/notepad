package com.mynote.repositories.elasticsearch.support;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public interface ElasticsearchAdmin {

    void createIndex();

    void putMapping();
}

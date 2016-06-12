package com.mynote.utils.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.EntityMapper;

import java.io.IOException;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class CustomEntityMapper implements EntityMapper {

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Override
    public String mapToString(Object object) throws IOException {
        return jacksonObjectMapper.writeValueAsString(object);
    }

    @Override
    public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
        return jacksonObjectMapper.readValue(source, clazz);
    }
}

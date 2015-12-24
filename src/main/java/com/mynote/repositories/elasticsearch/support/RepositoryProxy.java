package com.mynote.repositories.elasticsearch.support;

import com.mynote.config.session.HttpSessionContext;
import com.mynote.models.User;
import com.mynote.repositories.elasticsearch.NoteRepositoryImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.elasticsearch.ElasticsearchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
@Aspect
public class RepositoryProxy {

    static final Logger LOGGER = LoggerFactory.getLogger(NoteRepositoryImpl.class);

    private static final Set<User> INDICES_CACHE = Collections.synchronizedSet(new HashSet<>());

    @Autowired
    private HttpSessionContext httpSessionContext;

    @Before("execution(public * org.springframework.data.elasticsearch.repository.ElasticsearchRepository+.*(..))")
    public void createIndexIfNoteCreated(JoinPoint joinPoint) {
        User user = httpSessionContext.getUser();
        ElasticsearchAdmin repository = (ElasticsearchAdmin) joinPoint.getTarget();

        if (!INDICES_CACHE.contains(user)) {
            try {
                repository.createIndex();
                repository.putMapping();
                INDICES_CACHE.add(user);
            } catch (ElasticsearchException exception) {
                LOGGER.error("failed to load elasticsearch nodes : " + exception.getDetailedMessage());
            }
        }
    }
}

package com.mynote.utils.elasticsearch;

import com.mynote.repositories.elasticsearch.NoteRepositoryImpl;
import com.mynote.utils.CustomSessionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.elasticsearch.ElasticsearchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

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

    private static final Set<UserDetails> INDICES_CACHE = Collections.synchronizedSet(new HashSet<>());

    @Autowired
    private CustomSessionContext customSessionContext;

    @Before("execution(public * org.springframework.data.elasticsearch.repository.ElasticsearchRepository+.*(..))")
    public void createIndexIfNoteCreated(JoinPoint joinPoint) {
        UserDetails userDetails = customSessionContext.getUser();
        ElasticsearchAdmin repository = (ElasticsearchAdmin) joinPoint.getTarget();

        if (!INDICES_CACHE.contains(userDetails)) {
            try {
                repository.createIndex();
                repository.putMapping();
                INDICES_CACHE.add(userDetails);
            } catch (ElasticsearchException exception) {
                LOGGER.error("failed to load elasticsearch nodes : " + exception.getDetailedMessage());
            }
        }
    }
}

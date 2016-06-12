package com.mynote.utils.elasticsearch;

import com.mynote.models.Note;
import com.mynote.repositories.elasticsearch.NoteRepository;
import com.mynote.repositories.elasticsearch.NoteRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactory;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class CustomElasticsearchRepositoryFactory extends ElasticsearchRepositoryFactory {

    @Autowired
    protected ElasticsearchOperations elasticsearchTemplate;

    public CustomElasticsearchRepositoryFactory(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }

    public NoteRepository getNoteRepository() {
        return new NoteRepositoryImpl(getEntityInformation(Note.class), elasticsearchTemplate);
    }
}

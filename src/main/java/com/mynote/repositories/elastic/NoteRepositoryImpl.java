package com.mynote.repositories.elastic;

import com.mynote.models.Note;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteRepositoryImpl implements NoteRepositoryCustom {

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    @Override
    public Page<Note> find(Note note, Pageable pageable) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        NativeSearchQuery searchQuery;

        if (note.getSubject() != null) {
            boolQueryBuilder.must(matchQuery("subject", note.getSubject()));
        }
        if (note.getText() != null) {
            boolQueryBuilder.must(matchQuery("text", note.getText()));
        }

        searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageable)
                .build();

        return elasticsearchTemplate.queryForPage(searchQuery, Note.class);
    }
}

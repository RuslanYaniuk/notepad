package com.mynote.repositories.elasticsearch;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mynote.models.Note;
import org.elasticsearch.index.query.BaseQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteRepositoryImpl extends SimpleElasticsearchRepository<Note> implements NoteRepository {

    public static final String SUBJECT_FIELD = "subject";
    public static final String TEXT_FIELD = "text";

    public NoteRepositoryImpl(ElasticsearchEntityInformation<Note,
            String> metadata, ElasticsearchOperations elasticsearchOperations) {
        super.entityInformation = metadata;
        super.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public Page<Note> find(Note note, Pageable pageable) {
        NativeSearchQuery searchQuery;
        String textToSearch = null;
        BaseQueryBuilder queryBuilder;

        if (note.getSubject() != null) {
            textToSearch = note.getSubject();
        }
        if (note.getText() != null) {
            textToSearch = note.getText();
        }

        if (note.getId() != null) {
            queryBuilder = idsQuery("note").ids(note.getId());
        } else {
            queryBuilder = multiMatchQuery(textToSearch, getFieldsToSearch(note));
        }

        searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                .build();

        return elasticsearchOperations.queryForPage(searchQuery, Note.class);
    }

    @Override
    public Page<Note> getLatest(Pageable pageable) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSort(SortBuilders.fieldSort("creationDate").order(SortOrder.DESC))
                .withPageable(pageable)
                .build();

        return elasticsearchOperations.queryForPage(searchQuery, Note.class);
    }

    @Override
    public Page<Note> getByPrefixQuery(Note note, Pageable pageable) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        NativeSearchQuery searchQuery;

        if (note.getSubject() != null) {
            boolQueryBuilder.should(QueryBuilders.prefixQuery(SUBJECT_FIELD, note.getSubject()));
        }
        if (note.getText() != null) {
            boolQueryBuilder.should(QueryBuilders.prefixQuery(TEXT_FIELD, note.getText()));
        }

        searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageable)
                .build();

        return elasticsearchOperations.queryForPage(searchQuery, Note.class);
    }

    @Override
    public void createIndex() {
        elasticsearchOperations.createIndex(getEntityClass());
    }

    @Override
    public void putMapping() {
        elasticsearchOperations.putMapping(getEntityClass());
    }

    private String[] getFieldsToSearch(Note note) {
        List<String> fields = Lists.newArrayList();

        if (note.getSubject() != null) {
            fields.add(SUBJECT_FIELD);
        }
        if (note.getText() != null) {
            fields.add(TEXT_FIELD);
        }

        return Iterables.toArray(fields, String.class);
    }
}

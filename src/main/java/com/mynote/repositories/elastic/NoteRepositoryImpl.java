package com.mynote.repositories.elastic;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mynote.models.Note;
import org.elasticsearch.index.query.BaseQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.Assert;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteRepositoryImpl implements NoteRepositoryCustom {

    public static final String SUBJECT_FIELD = "subject";
    public static final String TEXT_FIELD = "text";

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    @Override
    public Page<Note> find(Note note, Pageable pageable) {
        List<String> fields = Lists.newArrayList();
        NativeSearchQuery searchQuery;
        String textToSearch = null;
        BaseQueryBuilder queryBuilder;

        Assert.notNull(note.getUserId());

        if (note.getSubject() != null) {
            fields.add(SUBJECT_FIELD);
            textToSearch = note.getSubject();
        }
        if (note.getText() != null) {
            fields.add(TEXT_FIELD);
            textToSearch = note.getText();
        }

        if (note.getId() != null) {
            queryBuilder = idsQuery("note").ids(note.getId());
        } else {
            queryBuilder = multiMatchQuery(textToSearch, Iterables.toArray(fields, String.class));
        }

        searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.filteredQuery(queryBuilder, getUserIdFilter(note.getUserId())))
                .withPageable(pageable)
                .build();

        return elasticsearchTemplate.queryForPage(searchQuery, Note.class);
    }

    public Page<Note> getLatest(Long userId, Pageable pageable) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.filteredQuery(matchAllQuery(), getUserIdFilter(userId)))
                .withSort(SortBuilders.fieldSort("creationDate").order(SortOrder.DESC))
                .withPageable(pageable)
                .build();

        return elasticsearchTemplate.queryForPage(searchQuery, Note.class);
    }

    @Override
    public boolean exists(Note note) {
        GetQuery query = new GetQuery();
        Long userId = note.getUserId();

        Assert.notNull(userId);

        query.setId(note.getId());
        note = elasticsearchTemplate.queryForObject(query, Note.class);

        if (note != null) {
            return note.getUserId().equals(userId);
        } else {
            return false;
        }
    }

    public TermFilterBuilder getUserIdFilter(Long userId) {
        return FilterBuilders.termFilter("userId", userId);
    }
}

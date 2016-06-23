package com.mynote.repositories.elasticsearch;

import com.mynote.models.Note;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;

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
        NativeSearchQuery sq;
        QueryBuilder qb;
        SortBuilder sb;

        if (note.getSubject() == null && note.getText() == null && note.getId() == null) {
            qb = matchAllQuery();
            sb = SortBuilders.fieldSort("creationDate").order(SortOrder.DESC);
        } else {
            BoolQueryBuilder bqb = QueryBuilders.boolQuery();

            if (note.getSubject() != null) {
                bqb.should(matchQuery(SUBJECT_FIELD, note.getSubject()));
            }
            if (note.getText() != null) {
                bqb.should(matchQuery(TEXT_FIELD, note.getText()));
            }
            if (note.getId() != null) {
                bqb.should(idsQuery("note").ids(note.getId()));
            }
            qb = bqb;
            sb = SortBuilders.scoreSort();
        }
        sq = new NativeSearchQueryBuilder()
                .withQuery(qb)
                .withPageable(pageable)
                .withSort(sb)
                .build();
        return elasticsearchOperations.queryForPage(sq, Note.class);
    }

    @Override
    public void createIndex() {
        elasticsearchOperations.createIndex(getEntityClass());
    }

    @Override
    public void putMapping() {
        elasticsearchOperations.putMapping(getEntityClass());
    }
}

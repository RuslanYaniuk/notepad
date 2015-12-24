package com.mynote.repositories.elasticsearch;

import com.mynote.models.Note;
import com.mynote.repositories.elasticsearch.support.ElasticsearchAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public interface NoteRepository extends ElasticsearchRepository<Note, String>, ElasticsearchAdmin {

    Page<Note> find(Note note, Pageable pageable);

    Page<Note> getLatest(Pageable pageable);

    Page<Note> getByPrefixQuery(Note note, Pageable pageable);
}

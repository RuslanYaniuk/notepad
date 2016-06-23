package com.mynote.repositories.elasticsearch;

import com.mynote.models.Note;
import com.mynote.utils.elasticsearch.ElasticsearchAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public interface NoteRepository extends ElasticsearchRepository<Note, String>, ElasticsearchAdmin {

    Page<Note> find(Note note, Pageable pageable);
}

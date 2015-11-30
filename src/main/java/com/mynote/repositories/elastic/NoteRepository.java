package com.mynote.repositories.elastic;

import com.mynote.models.Note;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public interface NoteRepository extends ElasticsearchCrudRepository<Note, String>, NoteRepositoryCustom {

    List<Note> findBySubjectLike(String subject);
}

package com.mynote.repositories.mongo;

import com.mynote.models.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public interface NoteRepository extends MongoRepository<Note, String>, NoteRepositoryCustom {

    List<Note> findByIdOrSubjectLikeOrTextLike(String id, String subject, String text);
}

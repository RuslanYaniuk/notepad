package com.mynote.repositories.mongo;

import com.mynote.models.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public interface NoteRepository extends MongoRepository<Note, String> {
}

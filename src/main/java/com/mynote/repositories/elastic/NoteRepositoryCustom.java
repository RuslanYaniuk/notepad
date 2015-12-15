package com.mynote.repositories.elastic;

import com.mynote.models.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public interface NoteRepositoryCustom {

    Page<Note> find(Note note, Long userId, Pageable pageable);

    Page<Note> getLatest(Long userId, Pageable pageable);
}

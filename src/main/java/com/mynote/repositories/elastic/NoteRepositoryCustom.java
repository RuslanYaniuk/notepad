package com.mynote.repositories.elastic;

import com.mynote.dto.note.NoteFindDTO;
import com.mynote.models.Note;
import org.springframework.data.domain.Page;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public interface NoteRepositoryCustom {

    Page<Note> find(NoteFindDTO noteFindDTO);
}

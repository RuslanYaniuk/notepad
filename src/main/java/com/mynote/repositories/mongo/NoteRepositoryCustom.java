package com.mynote.repositories.mongo;

import com.mynote.dto.note.NoteFindDTO;
import com.mynote.models.Note;

import java.util.List;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public interface NoteRepositoryCustom {

    List<Note> find(NoteFindDTO noteFindDTO);
}

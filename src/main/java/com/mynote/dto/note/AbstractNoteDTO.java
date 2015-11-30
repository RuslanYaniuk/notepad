package com.mynote.dto.note;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mynote.models.Note;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public abstract class AbstractNoteDTO {

    @JsonIgnore
    protected Note note = new Note();

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}

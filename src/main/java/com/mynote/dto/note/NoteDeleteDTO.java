package com.mynote.dto.note;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteDeleteDTO extends AbstractNoteDTO {

    @NotBlank
    public String getId() {
        return note.getId();
    }

    public void setId(String id) {
        note.setId(id);
    }
}

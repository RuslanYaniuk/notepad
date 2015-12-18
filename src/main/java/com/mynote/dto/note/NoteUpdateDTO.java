package com.mynote.dto.note;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteUpdateDTO extends AbstractNoteDTO {

    @NotBlank
    public String getId() {
        return note.getId();
    }

    public void setId(String id) {
        note.setId(id);
    }

    public String getSubject() {
        return note.getSubject();
    }

    public void setSubject(String subject) {
        note.setSubject(subject);
    }

    public String getText() {
        return note.getText();
    }

    public void setText(String text) {
        note.setText(text);
    }
}

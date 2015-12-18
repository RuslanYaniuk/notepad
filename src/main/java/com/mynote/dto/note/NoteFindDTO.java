package com.mynote.dto.note;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mynote.models.Note;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.PageImpl;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteFindDTO extends AbstractNoteDTO {

    private PageImpl<Note> page;

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

    public PageImpl<Note> getPage() {
        return page;
    }

    public void setPage(PageImpl<Note> page) {
        this.page = page;
    }

    @NotBlank
    @JsonIgnore
    public String getIdOrSubjectOrText() {
        if (getId() != null) {
            return getId();
        }
        if (getSubject() != null) {
            return getSubject();
        }
        if (getText() != null) {
            return getText();
        }
        return null;
    }
}

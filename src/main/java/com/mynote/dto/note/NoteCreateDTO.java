package com.mynote.dto.note;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteCreateDTO extends AbstractNoteDTO {

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

    @NotBlank
    @JsonIgnore
    public String getTextOrSubject() {
        if (!StringUtils.isBlank(getSubject())) {
            return getSubject();
        }
        if (!StringUtils.isBlank(getText())) {
            return getText();
        }
        return null;
    }
}

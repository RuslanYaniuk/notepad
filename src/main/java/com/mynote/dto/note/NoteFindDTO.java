package com.mynote.dto.note;

import com.mynote.models.Note;
import org.springframework.data.domain.PageImpl;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteFindDTO {

    private String id;

    private String subject;

    private String text;

    private PageImpl<Note> page;

    public NoteFindDTO() {
    }

    public NoteFindDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PageImpl<Note> getPage() {
        return page;
    }

    public void setPage(PageImpl<Note> page) {
        this.page = page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteFindDTO that = (NoteFindDTO) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "NoteFindDTO{" +
                "id='" + id + '\'' +
                '}';
    }
}

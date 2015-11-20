package com.mynote.dto.note;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteUpdateDTO {

    private String id;
    private String subject;
    private String text;

    public NoteUpdateDTO() {
    }

    public NoteUpdateDTO(String id, String subject, String text) {
        this.id = id;
        this.subject = subject;
        this.text = text;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteUpdateDTO that = (NoteUpdateDTO) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "NoteUpdateDTO{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

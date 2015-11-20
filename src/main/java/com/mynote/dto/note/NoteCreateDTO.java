package com.mynote.dto.note;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteCreateDTO {

    private String subject;
    private String text;

    public NoteCreateDTO() {
    }

    public NoteCreateDTO(String subject, String text) {
        this.subject = subject;
        this.text = text;
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

        NoteCreateDTO that = (NoteCreateDTO) o;

        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        return !(text != null ? !text.equals(that.text) : that.text != null);
    }

    @Override
    public int hashCode() {
        int result = subject != null ? subject.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NoteCreateDTO{" +
                "subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

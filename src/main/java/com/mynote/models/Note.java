package com.mynote.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Document(indexName = "mynote", type = "note")
public class Note {

    @Id
    private String id;

    @Field(type = FieldType.Long)
    private Long userId;

    @Field(type = FieldType.String)
    private String subject;

    @Field(type = FieldType.String)
    private String text;

    public Note() {
    }

    public Note(String id, String subject, String text) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

        Note note = (Note) o;

        return !(id != null ? !id.equals(note.id) : note.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

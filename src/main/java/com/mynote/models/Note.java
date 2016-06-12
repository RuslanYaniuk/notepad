package com.mynote.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mynote.utils.elasticsearch.ZonedDateTimeDeserializer;
import com.mynote.utils.elasticsearch.ZonedDateTimeSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Document(indexName = "mynote_#{customSessionContext.user.username}", type = "note")
public class Note {

    @Id
    private String id;

    @Field(type = FieldType.String)
    private String subject;

    @Field(type = FieldType.String)
    private String text;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime creationDate = ZonedDateTime.now(ZoneOffset.UTC);

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

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
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
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

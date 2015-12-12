package com.mynote.test.unit.services;

import com.mynote.models.Note;
import com.mynote.services.NoteService;
import com.mynote.test.utils.ElasticSearchUnit;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

import static com.mynote.test.utils.UserTestUtils.getUser2;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteServiceTests extends AbstractServiceTest {

    @Autowired
    private NoteService noteService;

    @Autowired
    private ElasticSearchUnit elasticSearchUnit;

    @Before
    public void setupElasticSearch() throws IOException, ExecutionException, InterruptedException {
        elasticSearchUnit.cleanInsertNotes();
    }

    @Test
    public void saveNote_CorrectDTO_NotedWithIdReturned() {
        Note note = new Note();

        note.setText("asdasd");
        note.setSubject("qweqwe");

        note = noteService.saveNote(note, getUser2());

        assertThat(note.getId().length(), greaterThan(0));
    }

    @Test
    public void updateNote_CorrectDTO_UpdatedNoteReturned() {
        Note note = new Note();

        note.setId("1");
        note.setText("Updated text");
        note.setSubject("Updated subject");

        note = noteService.updateNote(note);

        assertThat(note.getId(), is(note.getId()));
        assertThat(note.getSubject(), is(note.getSubject()));
        assertThat(note.getText(), is(note.getText()));
    }

    @Test(expected = NoSuchElementException.class)
    public void updateNote_NotExistentId_ExceptionThrown() {
        Note note = new Note();

        note.setId("8888");
        note.setText("Updated text");
        note.setSubject("Updated subject");

        noteService.updateNote(note);
    }

    @Test
    public void deleteNote_NoteId_NoteDeleted() {
        Note note = new Note();

        note.setId("3");

        noteService.deleteNote(note);

        Page notes = noteService.findAll(new PageRequest(1, 10));

        assertThat(notes.getTotalElements(), is(4L));
    }

    @Test
    public void findNote_NoteId_NoteReturned() {
        Note note = new Note();

        note.setId("3");

        Page<Note> page = noteService.findNotes(note, getUser2(), null);
        note = page.getContent().get(0);

        assertThat(note.getId(), is("3"));
        assertThat(note.getUserId(), is(2L));
        assertThat(note.getSubject(), is("subject 3 goes here. Third"));
    }

    @Test
    public void findNote_WordInSubject_OneNoteReturned() {
        Note note = new Note();

        note.setSubject("Third 3");

        Page<Note> page = noteService.findNotes(note, getUser2(), null);
        note = page.getContent().get(0);

        assertThat(page.getContent(), hasSize(1));
        assertTrue(note.getSubject().contains("Third"));
        assertTrue(note.getSubject().contains("3"));
    }
}

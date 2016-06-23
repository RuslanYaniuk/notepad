package com.mynote.test.unit.services;

import com.mynote.exceptions.NoteNotFoundException;
import com.mynote.models.Note;
import com.mynote.models.User;
import com.mynote.services.NoteService;
import com.mynote.test.conf.TestApplicationConfig;
import com.mynote.test.utils.ElasticsearchUnit;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutionException;

import static com.mynote.test.utils.UserTestUtils.getUser2;
import static com.mynote.test.utils.UserTestUtils.getUser3;
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
    private ElasticsearchUnit elasticsearchUnit;

    @Autowired
    private TestApplicationConfig testApplicationConfig;

    @Before
    public void setupElasticSearch() throws IOException, ExecutionException, InterruptedException {
        User user2 = getUser2();

        testApplicationConfig.getTestSessionContext().setMocked(true);
        testApplicationConfig.getTestSessionContext().setUser(user2);
        elasticsearchUnit.cleanInsertNotes(user2);
    }

    @Test
    public void saveNote_CorrectDTO_NotedWithIdReturned() {
        Note note = new Note();

        note.setText("asdasd");
        note.setSubject("qweqwe");
        note = noteService.saveNote(note);
        assertThat(note.getId().length(), greaterThan(0));
    }

    @Test
    public void updateNote_CorrectDTO_UpdatedNoteReturned() throws NoteNotFoundException {
        Note note = new Note();

        note.setId("1");
        note.setText("Updated text");
        note.setSubject("Updated subject");
        note = noteService.updateNote(note);
        assertThat(note.getId(), is(note.getId()));
        assertThat(note.getSubject(), is(note.getSubject()));
        assertThat(note.getText(), is(note.getText()));
    }

    @Test(expected = NoteNotFoundException.class)
    public void updateNote_NotExistentId_ExceptionThrown() throws NoteNotFoundException {
        Note note = new Note();

        note.setId("8888");
        note.setText("Updated text");
        note.setSubject("Updated subject");
        noteService.updateNote(note);
    }

    @Test
    public void deleteNote_NoteId_NoteDeleted() {
        Note note = new Note();
        Page notes;

        note.setId("3");
        noteService.deleteNote(note);
        notes = noteService.findAll(new PageRequest(1, 10));
        assertThat(notes.getTotalElements(), is(3L));
    }

    @Test
    public void findNote_NoteId_NoteReturned() {
        Note note = new Note();

        note.setId("3");
        note = noteService.findNotes(note, null).getContent().get(0);
        assertThat(note.getId(), is("3"));
        assertThat(note.getSubject(), is("subject 23 goes here. Third"));
    }

    @Test
    public void findNote_WordInSubject_OneNoteReturned() {
        Note note = new Note();
        Page<Note> page;

        note.setSubject("Third 3");
        page = noteService.findNotes(note, null);
        note = page.getContent().get(0);
        assertThat(page.getContent(), hasSize(1));
        assertTrue(note.getSubject().contains("Third"));
        assertTrue(note.getSubject().contains("3"));
    }

    @Test
    public void findNote_WordInText_OneNoteReturned() {
        Note note = new Note();
        Page<Note> page;

        note.setText("text22");
        page = noteService.findNotes(note, null);
        note = page.getContent().get(0);
        assertThat(page.getContent(), hasSize(1));
        assertTrue(note.getText().contains("xyz. text22."));
    }

    @Test
    public void findNote_TextAndSubject_OneNoteReturned() {
        Note note = new Note();
        Page<Note> page;
        String text = "text22";

        note.setText(text);
        note.setSubject("23");
        page = noteService.findNotes(note, null);
        assertThat(page.getContent(), hasSize(2));

        note.setText(text);
        note.setSubject(text);
        page = noteService.findNotes(note, null);
        assertThat(page.getContent(), hasSize(1));
    }

    @Test
    public void findNotes_EmptySearchFields_AllNotesSortedByDateDESCReturned() {
        Pageable pageable = new PageRequest(0, 20);
        Page<Note> page = noteService.findNotes(new Note(), pageable);
        ZonedDateTime firstNoteDateTime = page.getContent().get(0).getCreationDate();
        ZonedDateTime secondDateTime = page.getContent().get(1).getCreationDate();

        assertThat(page.getContent(), hasSize(4));
        assertTrue(firstNoteDateTime.isAfter(secondDateTime));
    }

    @Test
    public void findNotes_EmptySearchFieldsRequestFromUser3_PagesOrderedByDateReturned() throws InterruptedException, ExecutionException, IOException {
        testApplicationConfig.getTestSessionContext().setUser(getUser3());
        elasticsearchUnit.cleanInsertNotes(getUser3());

        Page<Note> secondPage;
        ZonedDateTime firstPageLastNoteDateTime;
        ZonedDateTime secondPageFirstNoteDateTime;
        Pageable pageable = new PageRequest(0, 10);
        Page<Note> firstPage = noteService.findNotes(new Note(), pageable);

        pageable = new PageRequest(1, 10);
        secondPage = noteService.findNotes(new Note(), pageable);

        firstPageLastNoteDateTime = firstPage.getContent().get(9).getCreationDate();
        secondPageFirstNoteDateTime = secondPage.getContent().get(0).getCreationDate();

        assertTrue(firstPageLastNoteDateTime.isAfter(secondPageFirstNoteDateTime));
    }
}

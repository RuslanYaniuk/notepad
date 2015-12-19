package com.mynote.test.unit.services;

import com.mynote.exceptions.NoteNotFoundException;
import com.mynote.models.Note;
import com.mynote.services.NoteService;
import com.mynote.test.utils.ElasticSearchUnit;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.mynote.test.utils.UserTestUtils.getUser2;
import static com.mynote.test.utils.UserTestUtils.getUser3;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

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
    public void updateNote_CorrectDTO_UpdatedNoteReturned() throws NoteNotFoundException {
        Note note = new Note();

        note.setId("1");
        note.setText("Updated text");
        note.setSubject("Updated subject");

        note = noteService.updateNote(note, getUser2());

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

        noteService.updateNote(note, getUser2());
    }

    @Test
    public void deleteNote_NoteId_NoteDeleted() {
        Note note = new Note();

        note.setId("3");

        noteService.deleteNote(note, getUser2());

        Page notes = noteService.findAll(new PageRequest(1, 10));

        assertThat(notes.getTotalElements(), is(23L));
    }

    @Test
    public void findNote_NoteId_NoteReturned() {
        Note note = new Note();

        note.setId("3");

        Page<Note> page = noteService.findNotes(note, getUser2(), null);
        note = page.getContent().get(0);

        assertThat(note.getId(), is("3"));
        assertThat(note.getUserId(), is(2L));
        assertThat(note.getSubject(), is("subject 23 goes here. Third"));
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

    @Test
    public void getLatest_PageRequestFor20Notes_20NotesReturned() {
        Pageable pageable = new PageRequest(0, 20);

        Page page = noteService.getLatest(getUser3().getId(), pageable);

        assertThat(page.getNumberOfElements(), is(20));
    }

    @Test
    public void getLatest_RequestForPage0_PageWithNotesSortedByDateDESCReturned() {
        Pageable pageable = new PageRequest(0, 20);

        Page<Note> page = noteService.getLatest(getUser3().getId(), pageable);

        ZonedDateTime firstNoteDateTime = page.getContent().get(0).getCreationDate();
        ZonedDateTime secondDateTime = page.getContent().get(1).getCreationDate();

        assertTrue(firstNoteDateTime.isAfter(secondDateTime));
    }

    @Test
    public void getLatest_RequestFor2Pages_FirstPageDatesAreAfterSecondPageDates() {
        Pageable pageable = new PageRequest(0, 10);

        Page<Note> firstPage = noteService.getLatest(getUser3().getId(), pageable);

        pageable = new PageRequest(1, 10);
        Page<Note> secondPage = noteService.getLatest(getUser3().getId(), pageable);

        ZonedDateTime firstPageNoteDateTime = firstPage.getContent().get(9).getCreationDate();
        ZonedDateTime secondPageDateTime = secondPage.getContent().get(0).getCreationDate();

        assertTrue(firstPageNoteDateTime.isAfter(secondPageDateTime));
    }

    @Test
    public void getWordSuggestion_WordPrefix_CompleteVariantsOfWordReturned() {
        String wordPrefix = "wor";
        Note note = new Note();

        note.setText(wordPrefix);

        Set<String> words = noteService.getWordsSuggestion(note, getUser3());

        assertThat(words, hasSize(3));

        for (String word : words) {
            assertTrue(StringUtils.startsWithIgnoreCase(word, wordPrefix));
            assertFalse(word.contains(" "));
        }
    }
}

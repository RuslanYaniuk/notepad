package com.mynote.test.unit.services;

import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.note.NoteFindDTO;
import com.mynote.dto.note.NoteUpdateDTO;
import com.mynote.models.Note;
import com.mynote.services.NoteService;
import com.mynote.test.utils.ElasticSearchUnit;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
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
        elasticSearchUnit.loadFixtures();
    }

    @Test
    public void saveNote_CorrectDTO_NotedWithIdReturned() {
        NoteCreateDTO noteCreateDTO = new NoteCreateDTO();

        noteCreateDTO.setText("asdasd");
        noteCreateDTO.setSubject("qweqwe");

        Note note = noteService.saveNote(noteCreateDTO);

        assertThat(note.getId().length(), greaterThan(0));
    }

    @Test
    public void updateNote_CorrectDTO_UpdatedNoteReturned() {
        NoteUpdateDTO noteUpdateDTO = new NoteUpdateDTO();

        noteUpdateDTO.setId("1");
        noteUpdateDTO.setText("Updated text");
        noteUpdateDTO.setSubject("Updated subject");

        Note note = noteService.updateNote(noteUpdateDTO);

        assertThat(note.getId(), is(noteUpdateDTO.getId()));
        assertThat(note.getSubject(), is(noteUpdateDTO.getSubject()));
        assertThat(note.getText(), is(noteUpdateDTO.getText()));
    }

    @Test
    public void deleteNote_NoteId_NoteDeleted() {
        NoteFindDTO noteFindDTO = new NoteFindDTO("3");

        noteService.deleteNote(noteFindDTO);

        Page notes = noteService.findAll(new PageRequest(1, 10));

        assertThat(notes.getTotalElements(), is(4L));
    }

    @Test
    public void findNote_NoteId_NoteReturned() {
        NoteFindDTO noteFindDTO = new NoteFindDTO();

        noteFindDTO.setId("3");

        Page<Note> page = noteService.findNotes(noteFindDTO);
        Note note = page.getContent().get(0);

        assertThat(note.getId(), is("3"));
        assertThat(note.getSubject(), is("subject 3 goes here. Third"));
    }

    @Test
    public void findNote_WordInSubject_OneNoteReturned() {
        NoteFindDTO noteFindDTO = new NoteFindDTO();

        noteFindDTO.setSubject("Third 3");

        Page<Note> page = noteService.findNotes(noteFindDTO);
        Note note = page.getContent().get(0);

        assertThat(page.getContent().size(), is(1));
        assertTrue(note.getSubject().contains("Third"));
        assertTrue(note.getSubject().contains("3"));
    }
}

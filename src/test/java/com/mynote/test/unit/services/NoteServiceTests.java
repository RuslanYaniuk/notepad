package com.mynote.test.unit.services;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.note.NoteFindDTO;
import com.mynote.dto.note.NoteUpdateDTO;
import com.mynote.models.Note;
import com.mynote.services.NoteService;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder.mongoDb;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteServiceTests extends AbstractServiceTest {

    @Autowired
    private NoteService noteService;

    @Rule
    public MongoDbRule remoteMongoDbRule = new MongoDbRule(mongoDb()
            .username("mynote")
            .password("mynote")
            .host("localhost")
            .databaseName("mynote_test")
            .build());

    @Test
    @UsingDataSet(locations = "/nosqlunit-datasets/notes.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void saveNote_CorrectDTO_NotedWithIdReturned() {
        NoteCreateDTO noteCreateDTO = new NoteCreateDTO();

        Note note = noteService.saveNote(noteCreateDTO);

        assertThat(note.getId().length(), greaterThan(0));
    }

    @Test
    @UsingDataSet(locations = "/nosqlunit-datasets/notes.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void updateNote_CorrectDTO_UpdatedNoteReturned() {
        NoteUpdateDTO noteUpdateDTO = new NoteUpdateDTO();

        noteUpdateDTO.setId("564cb8d9559a2ce2600f3c1f");
        noteUpdateDTO.setText("Updated text");
        noteUpdateDTO.setSubject("Updated subject");

        Note note = noteService.updateNote(noteUpdateDTO);

        assertThat(note.getId(), is(noteUpdateDTO.getId()));
        assertThat(note.getSubject(), is(noteUpdateDTO.getSubject()));
        assertThat(note.getText(), is(noteUpdateDTO.getText()));
    }

    @Test
    @UsingDataSet(locations = "/nosqlunit-datasets/notes.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void findNote_CorrectDTO_NoteReturned() {
        NoteFindDTO noteFindDTO = new NoteFindDTO("564cb8d9559a2ce2600f3c1f");

        Note note = noteService.findNote(noteFindDTO);

        assertThat(note.getSubject(), is("subject 1"));
        assertThat(note.getText(), is("text1"));
    }
}

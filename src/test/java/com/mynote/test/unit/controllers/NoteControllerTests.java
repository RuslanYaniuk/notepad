package com.mynote.test.unit.controllers;

import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.note.NoteDeleteDTO;
import com.mynote.dto.note.NoteFindDTO;
import com.mynote.dto.note.NoteUpdateDTO;
import com.mynote.test.utils.ElasticSearchUnit;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.mynote.config.web.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteControllerTests extends AbstractControllerTest {

    @Autowired
    private ElasticSearchUnit elasticSearchUnit;

    @Before
    public void loadFixtures() throws IOException, ExecutionException {
        elasticSearchUnit.cleanInsertNotes();
    }

    @Test
    public void createNote_CorrectNoteDTO_NoteCreated_200() throws Exception {
        NoteCreateDTO noteCreateDTO = new NoteCreateDTO();

        noteCreateDTO.setSubject("Test subject");
        noteCreateDTO.setText("test text......");

        createNote(noteCreateDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", not(StringUtils.EMPTY)))
                .andExpect(jsonPath("$.subject", is("Test subject")))
                .andExpect(jsonPath("$.text", is("test text......")));
    }

    @Test
    public void createNote_BlankFieldsDTO_ExceptionThrown_400() throws Exception {
        createNote(new NoteCreateDTO())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("NotBlank.noteCreateDTO.textOrSubject")));
    }

    @Test
    public void deleteNote_CorrectNoteFindDTO_NoteDeleted_200() throws Exception {
        NoteFindDTO noteFindDTO = new NoteFindDTO();

        noteFindDTO.setId("1");

        mockMvc.perform(delete("/api/note/delete")
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteFindDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("note.delete.success")));
    }

    @Test
    public void deleteNote_NotExistentNoteId_ErrorReturned_400() throws Exception {
        NoteDeleteDTO noteDeleteDTO = new NoteDeleteDTO();

        mockMvc.perform(delete("/api/note/delete")
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteDeleteDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("NotBlank.noteDeleteDTO.id")));
    }

    @Test
    public void updateNote_CorrectNoteDTO_NoteUpdated_200() throws Exception {
        NoteUpdateDTO noteUpdateDTO = new NoteUpdateDTO();

        noteUpdateDTO.setId("2");
        noteUpdateDTO.setSubject("Updated subject");
        noteUpdateDTO.setText("Updated text");

        mockMvc.perform(post("/api/note/update")
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("note.update.success")));
    }

    @Test
    public void updateNote_EmptyId_ErrorReturned_400() throws Exception {
        NoteUpdateDTO noteUpdateDTO = new NoteUpdateDTO();

        noteUpdateDTO.setId(null);
        noteUpdateDTO.setSubject("Updated subject");
        noteUpdateDTO.setText("Updated text");

        mockMvc.perform(post("/api/note/update")
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteUpdateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("NotBlank.noteUpdateDTO.id")));
    }

    @Test
    public void findNotes_CorrectNoteId_NoteUpdated_200() throws Exception {
        NoteFindDTO note = new NoteFindDTO();

        note.setId("3");

        findNote(note)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].subject", is("subject 3 goes here. Third")));
    }

    @Test
    public void findNotes_WordsInSubject_OneNoteReturned_200() throws Exception {
        NoteFindDTO noteFindDTO = new NoteFindDTO();

        noteFindDTO.setSubject("Second 2");

        findNote(noteFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].subject", is("subject 2 goes here. Second")));
    }

    @Test
    public void findNotes_WordsInText_OneNoteReturned_200() throws Exception {
        NoteFindDTO noteFindDTO = new NoteFindDTO();

        noteFindDTO.setText("2 dog");

        findNote(noteFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].text", is("text2. Dog")));
    }

    @Test
    public void findNotes_EmptyFieldsDTO_400() throws Exception {
        NoteFindDTO noteFindDTO = new NoteFindDTO();

        findNote(noteFindDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("NotBlank.noteFindDTO.idOrSubjectOrText")));
    }

    private ResultActions createNote(NoteCreateDTO noteCreateDTO) throws Exception {
        return mockMvc.perform(put("/api/note/create")
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteCreateDTO)));
    }

    private ResultActions findNote(NoteFindDTO note) throws Exception {
        return mockMvc.perform(post("/api/note/find")
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(note)));
    }
}

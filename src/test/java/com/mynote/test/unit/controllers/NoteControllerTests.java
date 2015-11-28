package com.mynote.test.unit.controllers;

import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.note.NoteFindDTO;
import com.mynote.dto.note.NoteUpdateDTO;
import com.mynote.test.utils.ElasticSearchUnit;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.mynote.config.web.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        elasticSearchUnit.loadFixtures();
    }

    @Test
    public void createNote_CorrectNoteDTO_NoteAdded_200() throws Exception {
        NoteCreateDTO noteCreateDTO = new NoteCreateDTO();

        noteCreateDTO.setSubject("Test subject");
        noteCreateDTO.setText("test text......");

        MvcResult result = mockMvc.perform(put("/api/note/create-note")
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteCreateDTO)))
                .andExpect(status().isOk()).andReturn();

        NoteFindDTO noteFindDTO = jacksonObjectMapper
                .readValue(result.getResponse().getContentAsString(), NoteFindDTO.class);

        assertFalse(StringUtils.isBlank(noteFindDTO.getId()));
    }

    @Test
    public void deleteNote_CorrectNoteFindDTO_NoteDeleted_200() throws Exception {
        NoteFindDTO noteFindDTO = new NoteFindDTO("1");

        MvcResult result = mockMvc.perform(delete("/api/note/delete-note")
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteFindDTO)))
                .andExpect(status().isOk()).andReturn();

        checkReturnedMessageCode(result, "note.delete.success");
    }

    @Test
    public void updateNote_CorrectNoteDTO_NoteUpdated_200() throws Exception {
        NoteUpdateDTO noteUpdateDTO = new NoteUpdateDTO();

        noteUpdateDTO.setId("2");
        noteUpdateDTO.setSubject("Updated subject");
        noteUpdateDTO.setText("Updated text");

        MvcResult result = mockMvc.perform(post("/api/note/update-note")
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteUpdateDTO)))
                .andExpect(status().isOk()).andReturn();

        checkReturnedMessageCode(result, "note.update.success");
    }

    @Test
    public void findNotes_CorrectNoteId_NoteUpdated_200() throws Exception {
        NoteFindDTO note = new NoteFindDTO();

        note.setId("3");

        MvcResult result = findNote(note);

        NoteUpdateDTO[] noteUpdateDTOs = jacksonObjectMapper
                .readValue(result.getResponse().getContentAsString(), NoteUpdateDTO[].class);

        assertNotNull(noteUpdateDTOs[0].getId());
        assertNotNull(noteUpdateDTOs[0].getSubject());
        assertNotNull(noteUpdateDTOs[0].getText());
    }

    @Test
    public void findNotes_WordInSubject_OneNoteReturned_200() throws Exception {
        NoteFindDTO noteFindDTO = new NoteFindDTO();

        noteFindDTO.setSubject("Third");

        MvcResult result = findNote(noteFindDTO);

        NoteUpdateDTO[] noteUpdateDTOs = jacksonObjectMapper
                .readValue(result.getResponse().getContentAsString(), NoteUpdateDTO[].class);
        assertThat(noteUpdateDTOs[0].getSubject(), is("subject 3 goes here. Third"));
    }

    private MvcResult findNote(NoteFindDTO note) throws Exception {
        return mockMvc.perform(post("/api/note/find-notes")
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(note)))
                .andExpect(status().isOk()).andReturn();
    }
}

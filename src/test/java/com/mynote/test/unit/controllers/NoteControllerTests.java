package com.mynote.test.unit.controllers;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.note.NoteFindDTO;
import com.mynote.dto.note.NoteUpdateDTO;
import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder.mongoDb;
import static com.mynote.config.web.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteControllerTests extends AbstractControllerTest {

    @Rule
    public MongoDbRule remoteMongoDbRule = new MongoDbRule(mongoDb()
            .username("mynote")
            .password("mynote")
            .host("localhost")
            .databaseName("mynote_test")
            .build());

    @Test
    @UsingDataSet(locations = "/nosqlunit-datasets/notes.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
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
    @UsingDataSet(locations = "/nosqlunit-datasets/notes.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void deleteNote_CorrectNoteFindDTO_NoteDeleted_200() throws Exception {
        NoteFindDTO noteFindDTO = new NoteFindDTO("564cb8d9559a2ce2600f3c1f");

        MvcResult result = mockMvc.perform(delete("/api/note/delete-note")
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteFindDTO)))
                .andExpect(status().isOk()).andReturn();

        checkReturnedMessageCode(result, "note.delete.success");
    }

    @Test
    @UsingDataSet(locations = "/nosqlunit-datasets/notes.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void updateNote_CorrectNoteDTO_NoteUpdated_200() throws Exception {
        NoteUpdateDTO noteUpdateDTO = new NoteUpdateDTO();

        noteUpdateDTO.setId("564cb8d9559a2ce2600f3c1f");
        noteUpdateDTO.setSubject("Updated subject");
        noteUpdateDTO.setText("Updated text");

        MvcResult result = mockMvc.perform(post("/api/note/update-note")
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteUpdateDTO)))
                .andExpect(status().isOk()).andReturn();

        checkReturnedMessageCode(result, "note.update.success");
    }

    @Test
    @UsingDataSet(locations = "/nosqlunit-datasets/notes.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void findNote_CorrectNoteDTO_NoteUpdated_200() throws Exception {
        NoteFindDTO note = new NoteFindDTO();

        note.setId("564cb8d9559a2ce2600f3c1f");

        MvcResult result = mockMvc.perform(post("/api/note/find-note")
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(note)))
                .andExpect(status().isOk()).andReturn();

        NoteUpdateDTO noteUpdateDTO = jacksonObjectMapper
                .readValue(result.getResponse().getContentAsString(), NoteUpdateDTO.class);

        assertNotNull(noteUpdateDTO.getId());
        assertNotNull(noteUpdateDTO.getSubject());
        assertNotNull(noteUpdateDTO.getText());
    }
}

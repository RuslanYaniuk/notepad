package com.mynote.test.unit.controllers;

import com.mynote.dto.common.PageRequestDTO;
import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.note.NoteDeleteDTO;
import com.mynote.dto.note.NoteFindDTO;
import com.mynote.dto.note.NoteUpdateDTO;
import com.mynote.test.utils.ElasticSearchUnit;
import com.mynote.test.utils.UserLoginDTOTestUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.ResultActions;

import static com.mynote.config.web.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteControllerTests extends AbstractSecuredControllerTest {

    @Autowired
    private ElasticSearchUnit elasticSearchUnit;

    @Before
    public void loadFixtures() throws Exception {
        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();

        elasticSearchUnit.cleanInsertNotes();

        loginUser(csrfTokenDTO, UserLoginDTOTestUtils.createUser2LoginDTO());
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
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteFindDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("note.delete.success")));
    }

    @Test
    public void deleteNote_NotExistentNoteId_ErrorReturned_400() throws Exception {
        NoteDeleteDTO noteDeleteDTO = new NoteDeleteDTO();

        mockMvc.perform(delete("/api/note/delete")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
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
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
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
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteUpdateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("NotBlank.noteUpdateDTO.id")));
    }

    @Test
    public void updateNote_IdOfDifferentUserNote_ErrorReturned_400() throws Exception {
        NoteUpdateDTO noteUpdateDTO = new NoteUpdateDTO();

        noteUpdateDTO.setId("32");
        noteUpdateDTO.setSubject("Updated subject");
        noteUpdateDTO.setText("Updated text");

        mockMvc.perform(post("/api/note/update")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteUpdateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(MESSAGE_CODE, is("note.lookup.notFound")));
    }

    @Test
    public void findNotes_CorrectNoteId_NoteUpdated_200() throws Exception {
        NoteFindDTO note = new NoteFindDTO();

        note.setId("3");

        findNote(note)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].subject", is("subject 23 goes here. Third")));
    }

    @Test
    public void findNotes_WordsInSubject_OneNoteReturned_200() throws Exception {
        NoteFindDTO noteFindDTO = new NoteFindDTO();

        noteFindDTO.setSubject("Second 2");

        findNote(noteFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].subject", is("subject 22 goes here. Second")));
    }

    @Test
    public void findNotes_WordsInText_OneNoteReturned_200() throws Exception {
        NoteFindDTO noteFindDTO = new NoteFindDTO();

        noteFindDTO.setText("xyz. not present text. abc");

        findNote(noteFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].text", is("xyz. text22. abc")));
    }

    @Test
    public void findNotes_EmptyFieldsDTO_400() throws Exception {
        NoteFindDTO noteFindDTO = new NoteFindDTO();

        findNote(noteFindDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("NotBlank.noteFindDTO.idOrSubjectOrText")));
    }

    @Test
    public void findNotes_LoggedUser_UserCanGetOnlyHisOwnNotes() throws Exception {
        buildWebAppContext();
        loginUser(csrfTokenDTO, UserLoginDTOTestUtils.createUser3LoginDTO());

        NoteFindDTO noteFindDTO = new NoteFindDTO();

        noteFindDTO.setSubject("goes");

        mockMvc.perform(post("/api/note/find")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteFindDTO)))
                .andExpect(jsonPath("$.content", hasSize(20)))
                .andExpect(jsonPath("$.content[0].id", is("AVFKmX5oLbYQMtdG0GOO")));
    }

    @Test
    public void getLatest_RequestForPageWith20Notes_PageWith18notesReturned() throws Exception {
        buildWebAppContext();
        loginUser(csrfTokenDTO, UserLoginDTOTestUtils.createUser3LoginDTO());

        Pageable page = new PageRequestDTO(0, 18);

        getLatestNotes(page)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(18)));
    }

    @Test
    public void getLatest_RequestForPageWith0PageSize_PageWith0notesReturned() throws Exception {
        buildWebAppContext();
        loginUser(csrfTokenDTO, UserLoginDTOTestUtils.createUser3LoginDTO());

        Pageable page = new PageRequestDTO(0, 0);

        getLatestNotes(page)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    private ResultActions getLatestNotes(Pageable page) throws Exception {
        return mockMvc.perform(get("/api/note/get-latest")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                .param("pageNumber", Integer.toString(page.getPageNumber()))
                .param("pageSize", Integer.toString(page.getPageSize())));
    }


    private ResultActions createNote(NoteCreateDTO noteCreateDTO) throws Exception {
        return mockMvc.perform(put("/api/note/create")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteCreateDTO)));
    }

    private ResultActions findNote(NoteFindDTO note) throws Exception {
        return mockMvc.perform(post("/api/note/find")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(note)));
    }
}

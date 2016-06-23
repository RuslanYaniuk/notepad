package com.mynote.test.functional.controllers;

import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.note.NoteDeleteDTO;
import com.mynote.dto.note.NoteFindDTO;
import com.mynote.dto.note.NoteUpdateDTO;
import com.mynote.models.Note;
import com.mynote.test.utils.ElasticsearchUnit;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.mynote.config.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static com.mynote.test.utils.UserTestUtils.getUser2;
import static com.mynote.test.utils.UserTestUtils.getUser3;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@WithMockUser(username = "user2@email.com", roles = {"USER"})
public class NoteControllerTests extends AbstractSecuredControllerTest {

    @Autowired
    private ElasticsearchUnit elasticsearchUnit;

    private NoteUpdateDTO noteUpdateDTO;
    private NoteCreateDTO noteCreateDTO;
    private NoteDeleteDTO noteDeleteDTO;
    private NoteFindDTO noteFindDTO;

    @Before
    public void loadFixtures() throws Exception {
        dbUnit.deleteAllFixtures();

        dbUnit.insertUsers();
        dbUnit.insertRoles();
        dbUnit.insertUsersToRoles();

        elasticsearchUnit.cleanInsertNotes(getUser2());

        noteUpdateDTO = new NoteUpdateDTO();
        noteCreateDTO = new NoteCreateDTO();
        noteDeleteDTO = new NoteDeleteDTO();
        noteFindDTO = new NoteFindDTO();
    }

    @Test
    public void createNote_CorrectNoteDTO_NoteCreated_200() throws Exception {
        noteCreateDTO.setSubject("Test subject");
        noteCreateDTO.setText("test text......");
        createNote(noteCreateDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", not(StringUtils.EMPTY)))
                .andExpect(jsonPath("$.creationDate", not(StringUtils.EMPTY)));
    }

    @Test
    public void createNote_BlankFieldsDTO_ExceptionThrown_400() throws Exception {
        createNote(new NoteCreateDTO())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(getFieldErrorCodes("textOrSubject"), hasItem(NOT_BLANK_CODE)));
    }

    @Test
    public void deleteNote_CorrectNoteDeleteDTO_NoteDeleted_200() throws Exception {
        noteDeleteDTO.setId("1");
        deleteNote(noteDeleteDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath($_MESSAGE_CODE, is("note.delete.success")));
    }

    @Test
    public void deleteNote_NotExistentNoteId_ErrorReturned_400() throws Exception {
        deleteNote(noteDeleteDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(getFieldErrorCodes("id"), hasItem(NOT_BLANK_CODE)));
    }

    @Test
    public void updateNote_CorrectNoteDTO_NoteUpdated_200() throws Exception {
        noteUpdateDTO.setId("2");
        noteUpdateDTO.setSubject("Updated subject");
        noteUpdateDTO.setText("Updated text");
        updateNote(noteUpdateDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath($_MESSAGE_CODE, is("note.update.success")));
    }

    @Test
    public void updateNote_CorrectNoteDTO_CreationDateRemainsTheSame() throws Exception {
        Note note = new Note();

        note.setId("2");
        note.setSubject("Updated subject");
        note.setText("Updated text");
        noteUpdateDTO.setNote(note);
        updateNote(noteUpdateDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath($_MESSAGE_CODE, is("note.update.success")));

        noteFindDTO.setNote(note);
        findNote(noteFindDTO)
                .andExpect(jsonPath("$.content[0].creationDate", is("2015-12-14T18:34:02.122Z")));
    }

    @Test
    public void updateNote_EmptyId_ErrorReturned_400() throws Exception {
        noteUpdateDTO.setId(null);
        noteUpdateDTO.setSubject("Updated subject");
        noteUpdateDTO.setText("Updated text");
        updateNote(noteUpdateDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(getFieldErrorCodes("id"), hasItem(NOT_BLANK_CODE)));
    }

    @Test
    public void updateNote_IdOfDifferentUserNote_ErrorReturned_400() throws Exception {
        noteUpdateDTO.setId("32");
        noteUpdateDTO.setSubject("Updated subject");
        noteUpdateDTO.setText("Updated text");
        updateNote(noteUpdateDTO)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath($_MESSAGE_CODE, is("note.lookup.notFound")));
    }

    @Test
    public void findNotes_CorrectNoteId_NoteUpdated_200() throws Exception {
        noteFindDTO.setId("3");
        findNote(noteFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].subject", is("subject 23 goes here. Third")));
    }

    @Test
    public void findNotes_WordsInSubject_OneNoteReturned_200() throws Exception {
        noteFindDTO.setSubject("Second 2");
        findNote(noteFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].subject", is("subject 22 goes here. Second")));
    }

    @Test
    public void findNotes_WordsInText_OneNoteReturned_200() throws Exception {
        noteFindDTO.setText("xyz. not present text. abc");
        findNote(noteFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].text", is("xyz. text22. abc")));
    }

    @Test
    public void findNotes_SubjectAndText_OneNoteReturned_200() throws Exception {
        noteFindDTO.setText("xyz. not present text. abc");
        noteFindDTO.setSubject("Third 23");
        findNote(noteFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    public void findNotes_NoteId_CorrectlyFormattedUTCDateReturned_200() throws Exception {
        noteFindDTO.setId("2");
        findNote(noteFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].creationDate", is("2015-12-14T18:34:02.122Z")));
    }

    @Test
    public void findNotes_EmptyFieldsDTO_ReturnedAllNotesSortedByTimeDesc() throws Exception {
        findNote(noteFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(4)))
                .andExpect(jsonPath("$.content[0].id", is("1")))
                .andExpect(jsonPath("$.content[1].id", is("4")))
                .andExpect(jsonPath("$.content[2].id", is("3")));
    }

    @Test
    @WithMockUser(username = "user3@email.com", roles = {"USER"})
    public void findNotes_User3RequestForPage2_Page2Returned() throws Exception {
        elasticsearchUnit.cleanInsertNotes(getUser3());

        noteFindDTO.setPageSize(10);
        noteFindDTO.setPageNumber(1);
        findNote(noteFindDTO)
                .andExpect(jsonPath("$.content", hasSize(10)));
    }

    @Test
    @WithMockUser(username = "user3@email.com", roles = {"USER"})
    public void findNotes_LoggedUser3_UserCanGetOnlyHisOwnNotes() throws Exception {
        elasticsearchUnit.cleanInsertNotes(getUser3());

        noteFindDTO.setSubject("goes");
        findNote(noteFindDTO)
                .andExpect(jsonPath("$.content", hasSize(20)))
                .andExpect(jsonPath("$.content[0].id", is("AVFKmX5oLbYQMtdG0GOO")));
    }

    private ResultActions deleteNote(NoteDeleteDTO noteDeleteDTO) throws Exception {
        return mockMvc.perform(delete("/api/note/delete").with(csrf())
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteDeleteDTO)));
    }

    private ResultActions updateNote(NoteUpdateDTO noteUpdateDTO) throws Exception {
        return mockMvc.perform(post("/api/note/update").with(csrf())
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteUpdateDTO)));
    }

    private ResultActions createNote(NoteCreateDTO noteCreateDTO) throws Exception {
        return mockMvc.perform(put("/api/note/create").with(csrf())
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(noteCreateDTO)));
    }

    private ResultActions findNote(NoteFindDTO noteFind) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/api/note/find");

        if (noteFind.getSubject() != null) {
            requestBuilder = requestBuilder.param("subject", noteFind.getSubject());
        }
        if (noteFind.getText() != null) {
            requestBuilder = requestBuilder.param("text", noteFind.getText());
        }
        if (noteFind.getId() != null) {
            requestBuilder = requestBuilder.param("id", noteFind.getId());
        }
        requestBuilder = requestBuilder.param("pageNumber", Integer.toString(noteFind.getPageNumber()));
        requestBuilder = requestBuilder.param("pageSize", Integer.toString(noteFind.getPageSize()));
        return mockMvc.perform(requestBuilder);
    }
}

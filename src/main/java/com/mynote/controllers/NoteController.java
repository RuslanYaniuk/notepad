package com.mynote.controllers;

import com.mynote.dto.common.PageRequestDTO;
import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.note.NoteDeleteDTO;
import com.mynote.dto.note.NoteFindDTO;
import com.mynote.dto.note.NoteUpdateDTO;
import com.mynote.exceptions.NoteNotFoundException;
import com.mynote.models.Note;
import com.mynote.models.User;
import com.mynote.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mynote.utils.UserSessionUtil.getCurrentUser;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@RestController
@RequestMapping(value = "/api/note")
public class NoteController extends AbstractController {

    @Autowired
    private NoteService noteService;

    @RequestMapping(value = "/create", method = PUT)
    public ResponseEntity createNote(@Validated @RequestBody NoteCreateDTO noteDTO) {
        return ok(noteService.saveNote(noteDTO.getNote(), getCurrentUser()));
    }

    @RequestMapping(value = "/find", method = POST)
    public ResponseEntity findNotes(@Validated @RequestBody NoteFindDTO noteFindDTO) {
        Note note = noteFindDTO.getNote();
        User user = getCurrentUser();
        Pageable page = (Pageable) noteFindDTO.getPage();

        return ok(noteService.findNotes(note, user, page));
    }

    @RequestMapping(value = "/update", method = POST)
    public ResponseEntity updateNote(@Validated @RequestBody NoteUpdateDTO noteUpdateDTO) throws NoteNotFoundException {
        noteService.updateNote(noteUpdateDTO.getNote(), getCurrentUser());

        return messageOK("note.update.success");
    }

    @RequestMapping(value = "/delete", method = DELETE)
    public ResponseEntity deleteNote(@Validated @RequestBody NoteDeleteDTO noteDeleteDTO) {
        noteService.deleteNote(noteDeleteDTO.getNote(), getCurrentUser());

        return messageOK("note.delete.success");
    }

    @RequestMapping(value = "/get-latest", method = GET)
    public ResponseEntity getLatest(PageRequestDTO pageRequest) {
        return ok(noteService.getLatest(getCurrentUser().getId(), pageRequest));
    }

    @RequestMapping(value = "/get-suggested-words", method = GET)
    public ResponseEntity getSuggestedWords(@Validated NoteFindDTO noteFindDTO) {
        return ok(noteService.getWordsSuggestion(noteFindDTO.getNote(), getCurrentUser()));
    }
}

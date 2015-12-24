package com.mynote.controllers;

import com.mynote.dto.common.PageRequestDTO;
import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.note.NoteDeleteDTO;
import com.mynote.dto.note.NoteFindDTO;
import com.mynote.dto.note.NoteUpdateDTO;
import com.mynote.exceptions.NoteNotFoundException;
import com.mynote.models.Note;
import com.mynote.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ok(noteService.saveNote(noteDTO.getNote()));
    }

    @RequestMapping(value = "/find", method = POST)
    public ResponseEntity findNotes(@Validated @RequestBody NoteFindDTO noteFindDTO) {
        Note note = noteFindDTO.getNote();
        Pageable page = (Pageable) noteFindDTO.getPage();

        return ok(noteService.findNotes(note, page));
    }

    @RequestMapping(value = "/update", method = POST)
    public ResponseEntity updateNote(@Validated @RequestBody NoteUpdateDTO noteUpdateDTO) throws NoteNotFoundException {
        noteService.updateNote(noteUpdateDTO.getNote());

        return messageOK("note.update.success");
    }

    @RequestMapping(value = "/delete", method = DELETE)
    public ResponseEntity deleteNote(@Validated @RequestBody NoteDeleteDTO noteDeleteDTO) {
        noteService.deleteNote(noteDeleteDTO.getNote());

        return messageOK("note.delete.success");
    }

    @RequestMapping(value = "/get-latest", method = GET)
    public ResponseEntity getLatest(PageRequestDTO pageRequest) {
        return ok(noteService.getLatest(pageRequest));
    }
}

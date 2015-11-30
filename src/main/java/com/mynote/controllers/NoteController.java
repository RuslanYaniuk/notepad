package com.mynote.controllers;

import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.note.NoteFindDTO;
import com.mynote.dto.note.NoteUpdateDTO;
import com.mynote.models.Note;
import com.mynote.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity createNote(@RequestBody NoteCreateDTO noteDTO) {
        Note note = noteService.saveNote(noteDTO);
        NoteFindDTO noteFindDTO = new NoteFindDTO();

        noteFindDTO.setId(note.getId());

        return ok(noteFindDTO);
    }

    @RequestMapping(value = "/find", method = POST)
    public ResponseEntity findNotes(@RequestBody NoteFindDTO noteFindDTO) {
        Page<Note> notes = noteService.findNotes(noteFindDTO);

        return ok(notes);
    }

    @RequestMapping(value = "/update", method = POST)
    public ResponseEntity updateNote(@RequestBody NoteUpdateDTO noteUpdateDTO) {
        noteService.updateNote(noteUpdateDTO);

        return messageOK("note.update.success");
    }

    @RequestMapping(value = "/delete", method = DELETE)
    public ResponseEntity deleteNote(@RequestBody NoteFindDTO noteFindDTO) {
        noteService.deleteNote(noteFindDTO);

        return messageOK("note.delete.success");
    }
}

package com.mynote.controllers;

import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.note.NoteFindDTO;
import com.mynote.dto.note.NoteUpdateDTO;
import com.mynote.models.Note;
import com.mynote.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/create-note", method = PUT)
    public ResponseEntity createNote(@RequestBody NoteCreateDTO noteDTO) {
        Note note = noteService.saveNote(noteDTO);
        NoteFindDTO noteFindDTO = new NoteFindDTO();

        noteFindDTO.setId(note.getId());

        return ok(noteFindDTO);
    }

    @RequestMapping(value = "/find-note", method = POST)
    public ResponseEntity findNote(@RequestBody NoteFindDTO noteFindDTO) {
        Note note = noteService.findNote(noteFindDTO);

        return ok(note);
    }

    @RequestMapping(value = "/update-note", method = POST)
    public ResponseEntity updateNote(@RequestBody NoteUpdateDTO noteUpdateDTO) {
        noteService.updateNote(noteUpdateDTO);

        return messageOK("note.update.success");
    }

    @RequestMapping(value = "/delete-note", method = DELETE)
    public ResponseEntity deleteNote(@RequestBody NoteFindDTO noteFindDTO) {
        noteService.deleteNote(noteFindDTO);

        return messageOK("note.delete.success");
    }
}

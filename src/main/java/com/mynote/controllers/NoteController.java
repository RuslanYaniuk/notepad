package com.mynote.controllers;

import com.mynote.config.web.ExtendedMessageSource;
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

import java.util.Locale;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@RestController
@RequestMapping(value = "/api/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private ExtendedMessageSource messageSource;

    @RequestMapping(value = "/create-note", method = PUT, produces = "application/json;charset=UTF-8")
    public ResponseEntity createNote(@RequestBody NoteCreateDTO noteDTO) {
        Note note = noteService.saveNote(noteDTO);
        NoteFindDTO noteFindDTO = new NoteFindDTO();

        noteFindDTO.setId(note.getId());

        return new ResponseEntity<>(noteFindDTO, OK);
    }

    @RequestMapping(value = "/find-note", method = POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity findNote(@RequestBody NoteFindDTO noteFindDTO) {
        Note note = noteService.findNote(noteFindDTO);

        return new ResponseEntity<>(note, OK);
    }

    @RequestMapping(value = "/update-note", method = POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity updateNote(@RequestBody NoteUpdateDTO noteUpdateDTO, Locale locale) {
        noteService.updateNote(noteUpdateDTO);

        return new ResponseEntity<>(messageSource.getMessageDTO("note.update.success", locale), OK);
    }

    @RequestMapping(value = "/delete-note", method = DELETE, produces = "application/json;charset=UTF-8")
    public ResponseEntity deleteNote(@RequestBody NoteFindDTO noteFindDTO, Locale locale) {
        noteService.deleteNote(noteFindDTO);

        return new ResponseEntity<>(messageSource.getMessageDTO("note.delete.success", locale), OK);
    }
}

package com.mynote.services;

import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.note.NoteFindDTO;
import com.mynote.dto.note.NoteUpdateDTO;
import com.mynote.models.Note;
import com.mynote.repositories.mongo.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public Note saveNote(NoteCreateDTO noteDTO) {
        Note note = new Note();

        note.setSubject(noteDTO.getSubject());
        note.setText(noteDTO.getText());

        return noteRepository.save(note);
    }

    public void deleteNote(NoteFindDTO noteFindDTO) {
        noteRepository.delete(noteFindDTO.getId());
    }

    public Note updateNote(NoteUpdateDTO noteUpdateDTO) {
        Note note = noteRepository.findOne(noteUpdateDTO.getId());

        note.setText(noteUpdateDTO.getText());
        note.setSubject(noteUpdateDTO.getSubject());

        return noteRepository.save(note);
    }

    public Note findNote(NoteFindDTO noteFindDTO) {
        return noteRepository.findOne(noteFindDTO.getId());
    }
}

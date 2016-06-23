package com.mynote.services;

import com.mynote.exceptions.NoteNotFoundException;
import com.mynote.models.Note;
import com.mynote.repositories.elasticsearch.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Service
public class NoteService {

    public static final int NOTES_PER_PAGE = 20;

    @Autowired
    private NoteRepository noteRepository;

    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    public Note updateNote(Note note) throws NoteNotFoundException {
        Note existentNote;

        if ((existentNote = noteRepository.findOne(note.getId())) == null) {
            throw new NoteNotFoundException();
        }
        existentNote.setSubject(note.getSubject());
        existentNote.setText(note.getText());
        return noteRepository.save(existentNote);
    }

    public Page<Note> findNotes(Note note, Pageable pageable) {
        int pageNumber = 0;

        if (pageable == null) {
            pageable = new PageRequest(pageNumber, NOTES_PER_PAGE);
        }
        return noteRepository.find(note, pageable);
    }

    public Page<Note> findAll(Pageable pageable) {
        return noteRepository.findAll(pageable);
    }

    public void deleteNote(Note note) {
        noteRepository.delete(note);
    }
}

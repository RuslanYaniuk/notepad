package com.mynote.services;

import com.mynote.exceptions.NoteNotFoundException;
import com.mynote.models.Note;
import com.mynote.models.User;
import com.mynote.repositories.elastic.NoteRepository;
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

    public Note saveNote(Note note, User owner) {
        note.setUserId(owner.getId());
        return noteRepository.save(note);
    }

    public void deleteNote(Note note, User owner) {
        note.setUserId(owner.getId());
        noteRepository.delete(note);
    }

    public Note updateNote(Note note, User owner) throws NoteNotFoundException {
        note.setUserId(owner.getId());
        if (!noteRepository.exists(note)) {
            throw new NoteNotFoundException();
        }
        return noteRepository.save(note);
    }

    public Page<Note> findNotes(Note note, User owner, Pageable pageable) {
        int pageNumber = 0;

        if (pageable == null) {
            pageable = new PageRequest(pageNumber, NOTES_PER_PAGE);
        }
        note.setUserId(owner.getId());

        return noteRepository.find(note, pageable);
    }

    public Page<Note> findAll(Pageable pageable) {
        return noteRepository.findAll(pageable);
    }

    public Page<Note> getLatest(Long ownerId, Pageable pageable) {
        return noteRepository.getLatest(ownerId, pageable);
    }
}

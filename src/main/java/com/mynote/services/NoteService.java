package com.mynote.services;

import com.mynote.models.Note;
import com.mynote.models.User;
import com.mynote.repositories.elastic.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Service
public class NoteService {

    public static final int NOTES_PER_PAGE = 20;
    @Autowired
    private NoteRepository noteRepository;

    public Note saveNote(Note note, User user) {
        note.setUserId(user.getId());
        return noteRepository.save(note);
    }

    public void deleteNote(Note note) {
        noteRepository.delete(note);
    }

    public Note updateNote(Note note) {
        if (!noteRepository.exists(note.getId())) {
            throw new NoSuchElementException("Can not update. Note does not exist");
        }
        return noteRepository.save(note);
    }

    public Page<Note> findNotes(Note note, User user, Pageable pageable) {
        Long userId = user.getId();
        int pageNumber = 0;

        if (pageable == null) {
            pageable = new PageRequest(pageNumber, NOTES_PER_PAGE);
        }

        return noteRepository.find(note, userId, pageable);
    }

    public Page<Note> findAll(Pageable pageable) {
        return noteRepository.findAll(pageable);
    }

    public Page<Note> getLatest(Long userId, Pageable pageable) {
        return noteRepository.getLatest(userId, pageable);
    }
}

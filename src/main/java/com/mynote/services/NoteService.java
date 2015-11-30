package com.mynote.services;

import com.google.common.collect.Lists;
import com.mynote.models.Note;
import com.mynote.repositories.elastic.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public Note saveNote(Note note) {
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

    public Page<Note> findNotes(Note note, Pageable pageable) {
        String id;

        if ((id = note.getId()) != null) {
            return new PageImpl<>(Lists.newArrayList(noteRepository.findOne(id)));
        }

        return noteRepository.find(note, pageable);
    }

    public Page findAll(Pageable pageable) {
        return noteRepository.findAll(pageable);
    }
}

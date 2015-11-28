package com.mynote.services;

import com.google.common.collect.Lists;
import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.note.NoteFindDTO;
import com.mynote.dto.note.NoteUpdateDTO;
import com.mynote.models.Note;
import com.mynote.repositories.elastic.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Note> findNotes(NoteFindDTO noteFindDTO) {
        String id;

        if ((id = noteFindDTO.getId()) != null) {
            return Lists.newArrayList(noteRepository.findOne(id));
        }

        return noteRepository.findBySubjectLike(noteFindDTO.getSubject());
    }

    public Page findAll(Pageable pageable) {
        return noteRepository.findAll(pageable);
    }
}

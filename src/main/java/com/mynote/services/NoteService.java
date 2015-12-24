package com.mynote.services;

import com.mynote.exceptions.NoteNotFoundException;
import com.mynote.models.Note;
import com.mynote.repositories.elasticsearch.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Service
public class NoteService {

    public static final int NOTES_PER_PAGE = 20;

    @Autowired
    private NoteRepository noteRepositoryImpl;

    public Note saveNote(Note note) {
        return noteRepositoryImpl.save(note);
    }

    public Note updateNote(Note note) throws NoteNotFoundException {
        if (!noteRepositoryImpl.exists(note.getId())) {
            throw new NoteNotFoundException();
        }
        return noteRepositoryImpl.save(note);
    }

    public Page<Note> findNotes(Note note, Pageable pageable) {
        int pageNumber = 0;

        if (pageable == null) {
            pageable = new PageRequest(pageNumber, NOTES_PER_PAGE);
        }

        return noteRepositoryImpl.find(note, pageable);
    }

    public Page<Note> findAll(Pageable pageable) {
        return noteRepositoryImpl.findAll(pageable);
    }

    public Page<Note> getLatest(Pageable pageable) {
        return noteRepositoryImpl.getLatest(pageable);
    }

    public Set<String> getWordsSuggestion(Note note) {
        Page<Note> notes = noteRepositoryImpl.getByPrefixQuery(note, new PageRequest(0, 20));
        Set<String> suggestedWords = new HashSet<>();

        for (Note suggestedNote : notes.getContent()) {
            Set<String> suggestedSet = Collections.emptySet();

            if (note.getSubject() != null) {
                suggestedSet = getWordByPrefix(suggestedNote.getSubject(), note.getSubject());
            }
            if (note.getText() != null) {
                suggestedSet = getWordByPrefix(suggestedNote.getText(), note.getText());
            }
            suggestedWords.addAll(suggestedSet);
        }

        return suggestedWords;
    }

    public void deleteNote(Note note) {
        noteRepositoryImpl.delete(note);
    }

    private Set<String> getWordByPrefix(String input, String prefix) {
        Set<String> result = new HashSet<>();
        String prefixRegex = "\\b" + prefix + "\\w+";
        Pattern p = Pattern.compile(prefixRegex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(input);

        while (m.find()) {
            result.add(m.group());
        }

        return result;
    }
}

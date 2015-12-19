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

    public Set<String> getWordsSuggestion(Note note, User user) {
        Page<Note> notes = noteRepository.getByPrefixQuery(note, user.getId(), new PageRequest(0, 20));
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

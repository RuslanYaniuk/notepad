package com.mynote.exceptions;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class NoteNotFoundException extends ExceptionWithParams {

    public NoteNotFoundException() {
        super("note.lookup.notFound");
    }
}

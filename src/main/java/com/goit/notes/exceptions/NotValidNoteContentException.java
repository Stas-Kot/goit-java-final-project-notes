package com.goit.notes.exceptions;

public class NotValidNoteContentException extends RuntimeException {
    public NotValidNoteContentException(String message) {
        super(message);
    }
}

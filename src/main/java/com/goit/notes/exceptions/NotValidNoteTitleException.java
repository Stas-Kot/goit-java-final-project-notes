package com.goit.notes.exceptions;

public class NotValidNoteTitleException extends RuntimeException {
    public NotValidNoteTitleException(String message) {
        super(message);
    }
}

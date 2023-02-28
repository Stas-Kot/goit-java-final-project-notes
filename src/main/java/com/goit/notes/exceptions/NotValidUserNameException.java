package com.goit.notes.exceptions;

public class NotValidUserNameException extends RuntimeException {
    public NotValidUserNameException(String message) {
        super(message);
    }
}

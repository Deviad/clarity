package com.clarity.claritydispatcher.error.exceptions;

public class NoMessageReceivedException extends Exception {

    public NoMessageReceivedException() {
        super();
    }
    public NoMessageReceivedException(String errorMessage) {
        super(errorMessage);
    }
}

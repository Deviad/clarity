package com.clarity.claritydispatcher.error;

public class NoMessageReceived extends RuntimeException {

    public NoMessageReceived() {
        super();
    }
    public NoMessageReceived(String errorMessage) {
        super(errorMessage);
    }
}

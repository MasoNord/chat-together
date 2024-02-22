package org.masonord.exception;

public class PropertyNotFoundException extends RuntimeException{

    public PropertyNotFoundException() {}

    public PropertyNotFoundException(String message) {
        super(message);
    }

    public PropertyNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}

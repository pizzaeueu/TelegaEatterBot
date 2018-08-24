package com.telega.eatter.exceptions;

public class AlreadySubscribedException extends RuntimeException {
    public AlreadySubscribedException() {
        super("User has already subscribed");
    }
}

package com.cyber.online_books.exception.domain;

/**
 * @author Cyber_Group
 * @project book_online
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}

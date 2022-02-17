package com.cyber.online_books.exception.domain;

/**
 * @author Cyber_Group
 * @project book_online
 */
public class EmailExistException extends Exception {
    public EmailExistException(String message) {
        super(message);
    }
}

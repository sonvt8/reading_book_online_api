package com.cyber.online_books.exception.domain;

/**
 * @author Cyber_Group
 * @project book_online
 */
public class UsernameExistException extends Exception{
    public UsernameExistException(String message) {
        super(message);
    }
}

package com.cyber.online_books.exception.domain;

public class UserNotLoginException extends Exception{
    private static final String EXCEPTION_MESSAGE = "Bạn cần Đăng Nhập trước khi thực hiện hành động này";
    public UserNotLoginException() {
        super(EXCEPTION_MESSAGE);
    }

    public UserNotLoginException(String message) {
        super(message);
    }
}

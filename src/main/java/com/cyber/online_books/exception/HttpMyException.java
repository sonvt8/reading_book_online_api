package com.cyber.online_books.exception;

public class HttpMyException extends Exception {

    private static final String EXCEPTION_MESSAGE = "Bạn cần Đăng Nhập trước khi thực hiện hành động này";

    public HttpMyException() {
        super(EXCEPTION_MESSAGE);
    }

    public HttpMyException(String messeger) {
        super(messeger);
    }
}

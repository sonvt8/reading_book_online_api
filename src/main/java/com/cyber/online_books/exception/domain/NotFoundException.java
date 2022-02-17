package com.cyber.online_books.exception.domain;

public class NotFoundException extends Exception {

    private static final String EXCEPTION_MESSAGE = "Xin lỗi. Liên kết bạn tìm không có hoặc đã bị xóa.";

    public NotFoundException() {
        super(EXCEPTION_MESSAGE);
    }

    public NotFoundException(String message) {
        super(message);
    }

}

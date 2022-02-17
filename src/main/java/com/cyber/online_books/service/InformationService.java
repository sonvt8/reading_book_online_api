package com.cyber.online_books.service;

import com.cyber.online_books.entity.Information;

public interface InformationService {

    /**
     * Lấy Thông Tin Web
     *
     * @return Information- Nếu tồn tại Information/ null - nếu không tồn tại Information
     */
    Information getWebInfomation();
}

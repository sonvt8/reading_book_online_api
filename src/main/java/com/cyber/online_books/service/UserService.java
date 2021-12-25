package com.cyber.online_books.service;

import com.cyber.online_books.entity.User;

public interface UserService {
    //User findUserById(Integer id);
    User findUserByUsername(String username);
}

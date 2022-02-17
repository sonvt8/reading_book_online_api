package com.cyber.online_books.service;

import com.cyber.online_books.entity.Role;

import java.util.List;

public interface RoleService {

    /**
     * Lấy Toàn Bộ Danh sách Phân Quyền
     * @return List<Role>
     */
    List<Role> getAllRole();
}

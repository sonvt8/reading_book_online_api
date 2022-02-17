package com.cyber.online_books.service.impl;

import com.cyber.online_books.entity.Role;
import com.cyber.online_books.repository.RoleRepository;
import com.cyber.online_books.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Lấy Toàn Bộ Danh sách Phân Quyền
     * @return List<Role>
     */
    public List<Role> getAllRole(){
        return roleRepository.findAll();
    }
}

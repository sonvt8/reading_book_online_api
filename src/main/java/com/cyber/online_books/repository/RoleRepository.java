package com.cyber.online_books.repository;

import com.cyber.online_books.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Cyber_Group
 */
@Repository
public interface RoleRepository extends JpaRepository< Role, Integer > {
    Role findByName(String name);
}

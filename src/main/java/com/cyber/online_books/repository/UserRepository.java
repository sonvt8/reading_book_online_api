package com.cyber.online_books.repository;

import com.cyber.online_books.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Tìm kiếm User theo username
     *
     * @param username
     * @return Optional<User>
     */
    User findUserByUsername(String username);

    /**
     * Tìm kiếm User theo email
     *
     * @param email
     * @return Optional<User>
     */
    User findUserByEmail(String email);

    /**
     * Kiểm Tra Có tồn tại Display Name với điều kiện Khác userId không
     *
     * @param userId
     * @param newNick
     * @return true - nếu tồn tại user/ false - nếu không tồn tại user
     */
    boolean existsByIdNotAndDisplayName(Long userId, String newNick);
}

package com.cyber.online_books.repository;

import com.cyber.online_books.entity.Information;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InformationRepository extends JpaRepository<Information, Integer > {

    /**
     * Lấy Information đầu tiên
     * @return Optional<Information>
     */
    Optional<Information> findFirstByOrderByIdDesc();

}

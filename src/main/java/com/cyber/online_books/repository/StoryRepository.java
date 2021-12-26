package com.cyber.online_books.repository;

import com.cyber.online_books.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long > {
    Story findStoryByName(String name);
}

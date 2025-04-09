package com.example.base_server.repository;

import com.example.base_server.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ReviewRepository extends JpaRepository <Review, Long> {

    //Find reviews from book object
    Set<Review> findByBook_TitleContainingIgnoreCase(String title);

    Set<Review> findByBook_IdOrderByCreatedAtAsc(Long id);

    //Find reviews from user object
    Set<Review> findByUser_NameContainingIgnoreCase(String name);

    Set<Review> findByUser_IdOrderByCreatedAtAsc(Long id);
}

package com.example.base_server.repository;

import com.example.base_server.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository <Review, Long> {

    Page<Review> findByBook_IdOrderByCreatedAtAsc(Long id, Pageable pageable);

    //Find reviews from user object
    Page<Review> findByUser_NameContainingIgnoreCase(String name, Pageable pageable);

    Page<Review> findByUser_IdOrderByCreatedAtAsc(Long id, Pageable pageable);

    //Get a bool for an already existing review, used to block duplicates
    boolean existsByUser_IdAndBook_Id(Long userId, Long bookId);
}

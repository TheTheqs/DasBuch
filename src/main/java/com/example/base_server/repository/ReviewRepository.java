package com.example.base_server.repository;

import com.example.base_server.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    //Get list of all user reviews by user ID.
    List<Review> findByUserId(Long userId);
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);

    //Get list of all book reviews by book ID.
    List<Review> findByBookId(Long userId);
    List<Review> findByBookIdOrderByCreatedAtDesc(Long bookId);

    //Count methods
    long countByBookId(Long bookId);
    long countByUserId(Long userId);

    //Get score average
    @Query("SELECT AVG(r.score) FROM Review r WHERE r.book.id = :bookId")
    Double findAverageScoreByBookId(Long bookId);

    //Check if user has read the provided book
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
}

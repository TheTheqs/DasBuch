package com.example.base_server.service;

import com.example.base_server.enums.Role;
import com.example.base_server.model.Book;
import com.example.base_server.model.Review;
import com.example.base_server.model.User;
import com.example.base_server.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    //1- Save new Review
    public Review saveReview(Book book, User user, String commentary, int score, String sReadAt){
        if(book == null || user == null){
            throw new IllegalArgumentException("Book and User cannot be null!");
        }

        score = Math.clamp(score, 1, 5);

        commentary = (commentary == null || commentary.isEmpty()) ? "There is no commentary." : commentary;

        LocalDateTime readAt = (sReadAt == null || sReadAt.isEmpty())? null : parseMMYY(sReadAt);

        return reviewRepository.save(new Review(book, user, commentary, score, readAt));
    }

    //2- Get Review Methods.
    public Review getReview(Long id){
        return reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review with ID " + id + " not found."));
    }

    public List<Review> getBookReviews(Long bookId, boolean ordered){
        if(ordered){
            return reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId);
        }
        return reviewRepository.findByBookId(bookId);
    }

    public List<Review> getUserReviews(Long userId, boolean ordered){
        if(ordered){
            return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }
        return reviewRepository.findByUserId(userId);
    }

    @Transactional
    //3- Update method
    public Review updateReview(Long id, Book book, String commentary, int score, String sReadAt){

        Review review = getReview(id);
        review.setBook(book);
        if (book != null) {
            review.setBook(book);
        }

        score = Math.clamp(score, 1, 5);
        review.setScore(score);

        commentary = (commentary == null || commentary.isEmpty()) ? "There is no commentary." : commentary;
        review.setCommentary(commentary);

        LocalDateTime readAt = (sReadAt == null || sReadAt.isEmpty())? null : parseMMYY(sReadAt);
        review.setReadAt(readAt);

        return reviewRepository.save(review);
    }

    //4 Delete review
    @Transactional
    public void deleteReview(Long id, User user){
        Review review = getReview(id);
        if(review.getUser().equals(user) || user.getRole() == Role.ADMIN) {
            reviewRepository.deleteById(id);
            return;
        }
        throw new AccessDeniedException("Only the ADMIN and review owners can delete reviews!");
    }

    //Util function, format String to DateTime
    private LocalDateTime parseMMYY(String input) {
        if (input == null || input.length() != 4) {
            throw new IllegalArgumentException("Invalid format. Expected MMYY.");
        }

        int month = Integer.parseInt(input.substring(0, 2));
        int yearShort = Integer.parseInt(input.substring(2, 4));

        int year = (yearShort < 30) ? (2000 + yearShort) : (1900 + yearShort);

        return LocalDateTime.of(year, Month.of(month), 1, 0, 0);
    }

}

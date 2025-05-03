package com.example.base_server.service;

import com.example.base_server.enums.Role;
import com.example.base_server.model.Book;
import com.example.base_server.model.Review;
import com.example.base_server.model.User;
import com.example.base_server.repository.BookRepository;
import com.example.base_server.repository.ReviewRepository;
import com.example.base_server.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class ReviewService {

    //Dependency injection
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    //CRUD methods
    //Create
    public Review createReview(User user, Book book, String synopsys, String commentary, int score, LocalDateTime readAt) {
        if (reviewRepository.existsByUser_IdAndBook_Id(user.getId(), book.getId())) {
            throw new IllegalStateException("User already reviewed this book.");
        }
        book.addUser(user); //For consistency on the readBy attribute
        return reviewRepository.save(new Review(user, bookRepository.save(book), synopsys, commentary, score, readAt));
    }

    //Read
    public Review getReview(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Review not found! id: " + id));
    }

    public Page<Review> getBookReviews(Long id, Pageable pageable) {
        if (!bookRepository.existsById(id)) {
            throw new NoSuchElementException("Review not found!");
        }
        //Return an empty list is not an error, but the book must exist in the data
        return reviewRepository.findByBook_IdOrderByCreatedAtAsc(id, pageable);
    }

    public Page<Review> getUserReviews(Long id, Pageable pageable) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("Error: user not found!");
        }
        //Again, result list can be empty, but the user must exist
        return reviewRepository.findByUser_IdOrderByCreatedAtAsc(id, pageable);
    }

    //Update
    public Review updateReview(User user, Long id, Book book, String synopsys, String commentary, int score, LocalDateTime readAt) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Review not found! id: " + id));
        if (!review.getUser().equals(user) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("You don't have permission to perform this action");
        }
        if (!review.getBook().equals(book) && book != null) {
            review.setBook(book);
        }
        if (!review.getSynopsys().equals(synopsys)) {
            review.setSynopsys(synopsys);
        }
        if (!review.getCommentary().equals(commentary)) {
            review.setCommentary(commentary);
        }
        if (review.getScore() != score) {
            review.setScore(score);
        }
        if (!review.getReadAt().equals(readAt)) {
            review.setReadAt(readAt);
        }
        return reviewRepository.save(review);
    }
    //Delete
    public boolean deleteReview(Long id, User user) {
        if (!reviewRepository.existsById(id)) {
            return false;
        }

        Review review = getReview(id);
        if(!Objects.equals(user.getId(), review.getUser().getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only the ADMIN and review owners can delete reviews");
        }

        review.getBook().removeUser(review.getUser());
        bookRepository.save(review.getBook());
        reviewRepository.delete(review);
        return true;
    }
}

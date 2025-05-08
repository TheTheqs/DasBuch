package com.example.base_server.controller;

import com.example.base_server.dto.BookDTO;
import com.example.base_server.dto.CreateReviewDTO;
import com.example.base_server.dto.ReviewDTO;
import com.example.base_server.model.Book;
import com.example.base_server.model.Review;
import com.example.base_server.model.User;
import com.example.base_server.service.BookService;
import com.example.base_server.service.ReviewService;
import com.example.base_server.utils.UserExtractor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final BookService bookService;
    private final ReviewService reviewService;


    public ReviewController(BookService bookService, ReviewService reviewService) {
        this.bookService = bookService;
        this.reviewService = reviewService;
    }

    //Create a new review
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(
            @RequestBody CreateReviewDTO dto,
            Authentication authentication) {

        User user = UserExtractor.extractUser(authentication);

        Book book = bookService.createBook(dto.getBookTitle(), dto.getAuthorsNames());

        Review review = reviewService.createReview(
                user,
                book,
                dto.getSynopsys(),
                dto.getCommentary(),
                dto.getScore(),
                dto.getReadAt()
        );

        return ResponseEntity.ok(new ReviewDTO(review));
    }

    //Get endpoints
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok( new ReviewDTO(reviewService.getReview(id)));
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Page<ReviewDTO>> getBookReviews(@PathVariable Long id, Pageable pageable) {
        Page<ReviewDTO> results = reviewService.getBookReviews(id, pageable).map(ReviewDTO::new);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Page<ReviewDTO>> getUserReviews(@PathVariable Long id, Pageable pageable) {
        Page<ReviewDTO> results = reviewService.getUserReviews(id, pageable).map(ReviewDTO::new);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ReviewDTO>> searchBookTitle(@RequestParam String title, Pageable pageable) {
        Page<ReviewDTO> results = reviewService.searchBookReviewsByBookTitle(title, pageable).map(ReviewDTO::new);
        return ResponseEntity.ok(results);
    }

    //Update review
    @PatchMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(
            @RequestBody CreateReviewDTO dto,
            @PathVariable Long id,
            Authentication authentication) {

        User user = UserExtractor.extractUser(authentication);

        Book book = bookService.createBook(dto.getBookTitle(), dto.getAuthorsNames());

        Review review = reviewService.updateReview(
                user,
                id,
                book,
                dto.getSynopsys(),
                dto.getCommentary(),
                dto.getScore(),
                dto.getReadAt()
        );

        return ResponseEntity.ok(new ReviewDTO(review));
    }

    //Delete review
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteReview(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(reviewService.deleteReview(id, UserExtractor.extractUser(authentication)));
    }
}

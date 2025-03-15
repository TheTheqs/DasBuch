package com.example.base_server.controller;

import com.example.base_server.dto.ReviewDTO;
import com.example.base_server.service.BookService;
import com.example.base_server.service.ReviewService;
import com.example.base_server.utils.UserExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserExtractor userExtractor;
    private final BookService bookService;

    public ReviewController(ReviewService reviewService, UserExtractor userExtractor, BookService bookService) {
        this.reviewService = reviewService;
        this.userExtractor = userExtractor;
        this.bookService = bookService;
    }

    @PostMapping("/add")
    public ResponseEntity<ReviewDTO> addReview(Authentication authentication,
                                               @RequestParam Long bookId,
                                               @RequestParam String commentary,
                                               @RequestParam int score,
                                               @RequestParam String readAt) {

        ReviewDTO reviewDTO = new ReviewDTO(reviewService.saveReview(
                bookService.getBookById(bookId),
                userExtractor.getUserFromAuth(authentication),
                commentary,
                score,
                readAt));

        return ResponseEntity.status(HttpStatus.CREATED).body(reviewDTO);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<?> getBookReviews(@PathVariable Long id) {
        return ResponseEntity.ok(ReviewDTO.generateReviewDTOList(reviewService.getBookReviews(id, true)));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserReviews(@PathVariable Long id) {
        return ResponseEntity.ok(ReviewDTO.generateReviewDTOList(reviewService.getUserReviews(id, true)));
    }

    @PatchMapping("/update")
    public ResponseEntity<ReviewDTO> updateReview(Authentication authentication,
                                               @RequestParam Long reviewId,
                                               @RequestParam Long bookId,
                                               @RequestParam String commentary,
                                               @RequestParam int score,
                                               @RequestParam String readAt) {

        ReviewDTO reviewDTO = new ReviewDTO(reviewService.updateReview(
                reviewId,
                userExtractor.getUserFromAuth(authentication),
                bookService.getBookById(bookId),
                commentary,
                score,
                readAt));

        return ResponseEntity.status(HttpStatus.OK).body(reviewDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(Authentication authentication, @PathVariable Long id){
        reviewService.deleteReview(id, userExtractor.getUserFromAuth(authentication));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Review deleted successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(new ReviewDTO(reviewService.getReview(id)));
    }

}

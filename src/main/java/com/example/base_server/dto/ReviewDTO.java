package com.example.base_server.dto;

import com.example.base_server.model.Author;
import com.example.base_server.model.Review;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ReviewDTO {

    private Long id;
    private String reviewBy;
    private String book;
    private String authors;
    private String commentary;
    private int score;
    private String readAt;

    public ReviewDTO(){}

    public ReviewDTO (Review review){
        this.id = review.getId();
        this.reviewBy = review.getUser().getName();
        this.book = review.getBook().getTitle();
        this.authors = formatAuthors(review.getBook().getAuthors());
        this.commentary = review.getCommentary();
        this.score = review.getScore();
        this.readAt = formatMonthYear(review.getReadAt());
    }
    //Util function for date format
    private String formatMonthYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "No read date provided.";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM, yyyy", Locale.of("pt", "BR"));
        return dateTime.format(formatter);
    }

    //Util function for authors format
    private String formatAuthors(List<Author> authors) {
        if(authors == null || authors.isEmpty()){
            throw new IllegalArgumentException("Authors list cannot be null or empty!");
        }
        return authors.stream()
                .map(Author::getName)
                .collect(Collectors.joining(", "));
    }

    public static List<ReviewDTO> generateReviewDTOList(List<Review> reviews) {
        return reviews.stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "id=" + id +
                ", reviewBy='" + reviewBy + '\'' +
                ", book='" + book + '\'' +
                ", authors='" + authors + '\'' +
                ", commentary='" + commentary + '\'' +
                ", score=" + score +
                ", readAt='" + readAt + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getReviewBy() {
        return reviewBy;
    }

    public String getBook() {
        return book;
    }

    public String getAuthors() {
        return authors;
    }

    public String getCommentary() {
        return commentary;
    }

    public int getScore() {
        return score;
    }

    public String getReadAt() {
        return readAt;
    }
}
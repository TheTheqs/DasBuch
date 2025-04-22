package com.example.base_server.dto;

import com.example.base_server.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public class CreateReviewDTO {

    @NotBlank
    private String bookTitle;

    @Size(min = 1)
    private List<String> authorsNames;

    private String synopsys;

    private String commentary;

    @Min(1) @Max(10)
    private int score;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime readAt;

    public CreateReviewDTO(String bookTitle, List<String> authorsNames, String synopsys, String commentary, int score, LocalDateTime readAt) {
        this.bookTitle = bookTitle;
        this.authorsNames = authorsNames;
        this.synopsys = synopsys;
        this.commentary = commentary;
        this.score = score;
        this.readAt = readAt;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public List<String> getAuthorsNames() {
        return authorsNames;
    }

    public void setAuthorsNames(List<String> authorsNames) {
        this.authorsNames = authorsNames;
    }

    public String getSynopsys() {
        return synopsys;
    }

    public void setSynopsys(String synopsys) {
        this.synopsys = synopsys;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
}

package com.example.base_server.dto;

import com.example.base_server.model.Review;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ReviewDTO {
    private final Long id;
    private final UserDTO user;
    private final BookDTO book;
    private final String synopsys;
    private final String commentary;
    private final float score;
    private final String readAt;

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.user = new UserDTO(review.getUser());
        this.book = new BookDTO(review.getBook());
        this.synopsys = review.getReaderSynopsis();
        this.commentary = review.getCommentary();
        this.score = ((float) review.getScore()) / 2f;
        this.readAt = formatMonthYear(review.getReadAt());
    }

    public Long getId() {
        return id;
    }

    public UserDTO getUser() {
        return user;
    }

    public BookDTO getBook() {
        return book;
    }

    public String getSynopsys() {
        return synopsys;
    }

    public String getCommentary() {
        return commentary;
    }

    public float getScore() {
        return score;
    }

    public String getReadAt() {
        return readAt;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "id=" + id +
                ", user=" + user +
                ", book=" + book +
                ", synopsys='" + synopsys + '\'' +
                ", commentary='" + commentary + '\'' +
                ", score=" + score +
                ", readAt='" + readAt + '\'' +
                '}';
    }

    //Util function
    public static String formatMonthYear(LocalDateTime dateTime) {
        if(dateTime == null) {
            return "No read at provided";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM, yyyy", Locale.ENGLISH);
        return dateTime.format(formatter);
    }
}

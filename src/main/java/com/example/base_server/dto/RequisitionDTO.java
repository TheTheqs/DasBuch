package com.example.base_server.dto;

import com.example.base_server.model.Requisition;

public class RequisitionDTO {

    private String title;
    private String author;
    private String user;

    public RequisitionDTO(){}

    public RequisitionDTO(Requisition requisition){
        this.title = requisition.getTitle();
        this.author = requisition.getAuthor();
        this.user = requisition.getUser().getEmail();
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Requisition {" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", requester='" + user + '\'' +
                '}';
    }
}

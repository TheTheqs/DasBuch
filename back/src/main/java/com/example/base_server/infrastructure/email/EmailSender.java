package com.example.base_server.infrastructure.email;

public interface EmailSender {
    void send(String to, String subject, String content);
}

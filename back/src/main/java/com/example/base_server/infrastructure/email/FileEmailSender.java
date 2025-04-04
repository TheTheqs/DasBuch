package com.example.base_server.infrastructure.email;

import com.example.base_server.infrastructure.file.TxtFileWriter;
import org.springframework.stereotype.Component;

@Component
public class FileEmailSender implements EmailSender {
    public void send(String to, String subject, String content) {
        String message = String.join("\n",
                "📧 Simulated Email Sent:",
                "To: " + to,
                "Subject: " + subject,
                content
        );
        TxtFileWriter.writeToFile("email_log.txt", message);
    }
}

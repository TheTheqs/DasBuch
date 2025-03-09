package com.example.base_server.service;

import com.example.base_server.utils.TxtFileUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailSender {

    //Main function
    public void sendEmail(String to, String subject, String body) {
        String message = ("_Email simulation_\nSending to: " + to + "\nSubject: " + subject + "\n" + body + "\n");
        writeLog(message);
    }

    //Log control
    private void writeLog(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        TxtFileUtil.write("email-system-log.txt", false, "[" + timestamp + "] " + message);
    }
}

package com.example.base_server.automation;

import com.example.base_server.client.GoogleBooksClient;
import com.example.base_server.service.BookService;
import com.example.base_server.utils.TxtFileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//This class will hold the actual automatic function, that's why it implements the AutomationTask interface
//Thi class is highly editable.
@Component
public class AutoTask implements AutomationTask {

    private final String AUTOMATION_TOKEN;
    private static final String API_URL = "http://localhost:8080/books/populate";

    private final RestTemplate restTemplate;

    BookService bookService;

    public AutoTask (RestTemplate restTemplate, BookService bookService,@Value("${automation.token}") String AUTOMATION_TOKEN) {
        this.restTemplate = restTemplate;
        this.AUTOMATION_TOKEN = AUTOMATION_TOKEN;
        this.bookService = bookService;
    }

    //This function will tell if the automation task continue or break
    @Override
    public boolean continueTask() {
        Long totalBooks = bookService.getTotalBooksNumber();
        writeLog("Total books in the database: " + totalBooks);
        return totalBooks <= 8L;
    }

    //The actual automatic call
    @Override
    public void execute() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Automation-Token", AUTOMATION_TOKEN);//Put token in the header

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        writeLog("API response: " + response.getBody());
    }

    //Log control
    private void writeLog(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        TxtFileUtil.write("automation-system-log.txt", false, "[" + timestamp + "] " + message);
    }
}

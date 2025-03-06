package com.example.base_server.controller;

import com.example.base_server.client.GoogleBooksClient;
import com.example.base_server.utils.TxtFileUtil;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloWorldController {

    @Autowired
    private GoogleBooksClient googleBooksClient;


    @GetMapping("/hello")
    public String sayHello() {
     return googleBooksClient.getNextGenre();
    }
}

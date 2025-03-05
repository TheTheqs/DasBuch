package com.example.base_server.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GoogleBooksClient {

    private final WebClient webClient;
    private final String apikey;

    public GoogleBooksClient(WebClient.Builder webClientBuilder,
                             @Value("${google.books.api.key}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl("https://www.googleapis.com/books/v1").build();
        this.apikey = apiKey;

    }

    public String searchBooks(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/volumes")
                        .queryParam("q", query)
                        .queryParam("key", apikey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();  // Makes sync call
    }

}

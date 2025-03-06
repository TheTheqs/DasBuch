package com.example.base_server.client;

import com.example.base_server.service.BookService;
import com.example.base_server.service.KeyWordService;
import com.example.base_server.utils.TxtFileUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleBooksClient {

    private final WebClient webClient;
    private final String apikey;
    private final BookService bookService;
    private final KeyWordService keyWordService;
    private List<String> BOOK_CATEGORIES = new ArrayList<>();

    //Constructor Dependency
    public GoogleBooksClient(WebClient.Builder webClientBuilder,
                             @Value("${google.books.api.key}") String apiKey,
                             BookService bookService, KeyWordService keyWordService) {
        this.webClient = webClientBuilder.baseUrl("https://www.googleapis.com/books/v1").build();
        this.apikey = apiKey;
        this.bookService = bookService;
        this.keyWordService = keyWordService;
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

    //Util function, convert ISBN 10 into ISBN 13 when the ISBN 13 is not available
    public static String convertIsbn10ToIsbn13(String isbn10) {
        if (isbn10 == null || isbn10.length() != 10) return null;

        String isbnBase = "978" + isbn10.substring(0, 9);
        int sum = 0;

        for (int i = 0; i < isbnBase.length(); i++) {
            int digit = Character.getNumericValue(isbnBase.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        return isbnBase + checkDigit;
    }

    //Get all books in Google Books by category.
    public boolean fetchBooksByCategory() {
        int startIndex = 0;
        int maxResults = 40; //Max allowed by Google Books
        String category = getNextGenre();
        ObjectMapper objectMapper = new ObjectMapper();

        while (true) {
            try {
                // Do the requisition
                int finalStartIndex = startIndex;
                String response = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/volumes")
                                .queryParam("q", "subject:" + category)
                                .queryParam("startIndex", finalStartIndex)
                                .queryParam("maxResults", maxResults)
                                .queryParam("key", apikey)
                                .build())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                //Convert response to json
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode items = rootNode.path("items");

                if (items.isMissingNode() || items.isEmpty()) {
                    //If there is no results left
                    System.out.println("Category " + category + " is finished!");
                    TxtFileUtil.write("progress.txt", false, category + ";");
                    break;
                }

                //Iterate the response
                for (JsonNode item : items) {
                    JsonNode volumeInfo = item.path("volumeInfo");

                    String title = volumeInfo.path("title").asText(null);
                    JsonNode authorsNode = volumeInfo.path("authors");
                    List<String> authors = authorsNode.isMissingNode() ? Collections.emptyList() :
                            objectMapper.convertValue(authorsNode, new TypeReference<List<String>>() {});

                    String description = volumeInfo.path("description").asText(null);
                    JsonNode categoriesNode = volumeInfo.path("categories");
                    List<String> keywords = categoriesNode.isMissingNode() ? Collections.emptyList() :
                            objectMapper.convertValue(categoriesNode, new TypeReference<List<String>>() {});

                    String publishedDateStr = volumeInfo.path("publishedDate").asText(null);
                    LocalDateTime publishedDate = parsePublishedDate(publishedDateStr);

                    String isbn = extractISBN(volumeInfo);
                    String coverURL = volumeInfo.path("imageLinks").path("thumbnail").asText(null);
                    String externalLink = item.path("selfLink").asText(null);

                    //Ignores if there is no ISBN
                    if (isbn == null) continue;

                    //Save in the database
                    bookService.saveBook(title, authors, description, keywords, publishedDate, isbn, coverURL, externalLink);
                }

                startIndex += maxResults; // Next pages

            } catch (Exception e) {
                System.err.println("Fail to get books by category" + category + ": " + e.getMessage());
                break;
            }
        }
        return true;
    }

    //Convert date from Google Books to a usable one
    private LocalDateTime parsePublishedDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return LocalDateTime.parse(dateStr + "-01-01T00:00:00");
        } catch (Exception e) {
            return null;
        }
    }

    //Format ISBN
    private String extractISBN(JsonNode volumeInfo) {
        JsonNode identifiers = volumeInfo.path("industryIdentifiers");
        if (identifiers.isArray()) {
            for (JsonNode id : identifiers) {
                if ("ISBN_13".equals(id.path("type").asText())) {
                    return id.path("identifier").asText();
                }
            }
            for (JsonNode id : identifiers) {
                if ("ISBN_10".equals(id.path("type").asText())) {
                    return convertIsbn10ToIsbn13(id.path("identifier").asText());
                }
            }
        }
        return null;
    }
    //Iteration function
    public String getNextGenre() {
        if(BOOK_CATEGORIES.isEmpty()) {
            BOOK_CATEGORIES = keyWordService.getAllKeywordValues();
        }
        //Getting the progress
        String actualProgress = TxtFileUtil.read("progress.txt");
        String lastGenre = Arrays.stream(actualProgress.split(";")).map(String::trim).toList().getLast();
        return BOOK_CATEGORIES.get(BOOK_CATEGORIES.indexOf(lastGenre) + 1);
    }
}

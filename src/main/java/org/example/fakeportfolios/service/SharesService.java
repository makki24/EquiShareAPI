package org.example.fakeportfolios.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class SharesService {

    @Value("${stock.indianKey}")
    private String key;

    @Value("${stock.baseUrl}")
    private String baseUrl;

    @Value("${stock.host}")
    private String host;

    public String search(String query) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + query))
                .header("x-rapidapi-key", key)
                .header("x-rapidapi-host", host)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}

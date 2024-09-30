package org.example.fakeportfolios.service;

import org.example.fakeportfolios.exception.DataInconsistentException;
import org.example.fakeportfolios.exception.DataNotFoundException;
import org.example.fakeportfolios.model.Portfolio;
import org.example.fakeportfolios.model.SharesTransaction;
import org.example.fakeportfolios.repository.SharesTransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class SharesService {

    private final SharesTransactionRepository sharesTransactionRepository;
    private final PortfolioService portfolioService;
    @Value("${stock.indianKey}")
    private String key;

    @Value("${stock.baseUrl}")
    private String baseUrl;

    @Value("${stock.host}")
    private String host;

    public SharesService(SharesTransactionRepository sharesTransactionRepository, PortfolioService portfolioService) {
        this.sharesTransactionRepository = sharesTransactionRepository;
        this.portfolioService = portfolioService;
    }

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

    @Transactional
    public ResponseEntity<Void> updateSharePrice(Long shareId, double amountToUpdate) {
        SharesTransaction sharesTransaction =
                sharesTransactionRepository.findById(shareId).orElseThrow(() -> new DataNotFoundException("SharesTransaction not found"));
        sharesTransaction.setCurrentPrice(amountToUpdate);
        sharesTransactionRepository.save(sharesTransaction);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Void> sellShare(Long portfolioId, Long shareId, double sellingPrice, int qty) {
        Portfolio portfolio = portfolioService.findById(portfolioId).orElseThrow(() -> new DataNotFoundException("Portfolio not found"));
        portfolio.updateTotalValue(qty * sellingPrice);

        portfolioService.save(portfolio);
        SharesTransaction sharesTransaction =
                sharesTransactionRepository.findById(shareId).orElseThrow(() -> new DataNotFoundException("SharesTransaction not found"));

        if (sharesTransaction.getQty() < qty)
            throw new DataInconsistentException("Qty should be greater than selling or equal to present qty");

        sharesTransaction.setQty(sharesTransaction.getQty() - qty);
        if (sharesTransaction.getQty() == 0) {
            sharesTransactionRepository.delete(sharesTransaction);
        }

        return ResponseEntity.noContent().build();
    }

}

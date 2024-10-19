package org.example.fakeportfolios.service;

import org.example.fakeportfolios.exception.DataInconsistentException;
import org.example.fakeportfolios.exception.DataNotFoundException;
import org.example.fakeportfolios.model.Portfolio;
import org.example.fakeportfolios.model.SharesTransaction;
import org.example.fakeportfolios.model.UserPortfolio;
import org.example.fakeportfolios.model.UserTransaction;
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
import java.time.LocalDateTime;

@Service
public class SharesService {

    private final SharesTransactionRepository sharesTransactionRepository;
    private final PortfolioService portfolioService;
    private final PortfolioTransactionService portfolioTransactionService;
    private final UserTransactionService userTransactionService;
    private final UserPortfolioService userPortfolioService;
    @Value("${stock.indianKey}")
    private String key;

    @Value("${stock.baseUrl}")
    private String baseUrl;

    @Value("${stock.host}")
    private String host;

    public SharesService(SharesTransactionRepository sharesTransactionRepository, PortfolioService portfolioService, PortfolioTransactionService portfolioTransactionService, UserTransactionService userTransactionService, UserPortfolioService userPortfolioService) {
        this.sharesTransactionRepository = sharesTransactionRepository;
        this.portfolioService = portfolioService;
        this.portfolioTransactionService = portfolioTransactionService;
        this.userTransactionService = userTransactionService;
        this.userPortfolioService = userPortfolioService;
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
    public ResponseEntity<Void> updateShareQtyAndBuyPrice(Long portfolioId, Long shareId, int qty, double amountToUpdate) {
        Portfolio portfolio = portfolioService.findById(portfolioId).orElseThrow(() -> new DataNotFoundException("Portfolio not found"));

        SharesTransaction sharesTransaction =
                sharesTransactionRepository.findById(shareId).orElseThrow(() -> new DataNotFoundException("SharesTransaction not found"));

        if ((portfolio.getTotalValue() + (sharesTransaction.getQty() * sharesTransaction.getBuyingPrice()))< (amountToUpdate * qty)) {
            throw new DataInconsistentException("Amount not availaible to update");
        }

        double netChange = (sharesTransaction.getQty() * sharesTransaction.getBuyingPrice()) - (amountToUpdate * qty);

        portfolio.setTotalValue(portfolio.getTotalValue() + netChange);

        long differenceQty = sharesTransaction.getQty() - qty;
        double differenceBuyingPrice = sharesTransaction.getBuyingPrice() - amountToUpdate;

        sharesTransaction.setQty(qty);
        sharesTransaction.setBuyingPrice(amountToUpdate);

        portfolioService.save(portfolio);
        sharesTransactionRepository.save(sharesTransaction);
        portfolioTransactionService.updateShareQtyAndBuyPrice(portfolio, sharesTransaction, differenceQty, differenceBuyingPrice);

        for (UserPortfolio userPortfolio: portfolio.getUserPortfolios()) {
            double userAmount = netChange * (userPortfolio.getOwnershipPercentage() / 100);
            userPortfolio.setContributionAmount(userPortfolio.getContributionAmount() + userAmount);
            userPortfolioService.save(userPortfolio);

            double updatedAmount = amountToUpdate - sharesTransaction.getCurrentPrice();
            updatedAmount = updatedAmount * (userPortfolio.getOwnershipPercentage() / 100);
            userTransactionService.updateShare(userPortfolio, -updatedAmount,
                    "more shares bought " + sharesTransaction.getDisplayName() + " current price deviated by " + updatedAmount
            );
        }

        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Void> updateSharePrice(Long shareId, double amountToUpdate) {
        SharesTransaction sharesTransaction =
                sharesTransactionRepository.findById(shareId).orElseThrow(() -> new DataNotFoundException("SharesTransaction not found"));


        double amount =
                (sharesTransaction.getQty() * amountToUpdate ) - (sharesTransaction.getQty() * sharesTransaction.getCurrentPrice());

        sharesTransaction.setCurrentPrice(amountToUpdate);
        sharesTransactionRepository.save(sharesTransaction);

        Portfolio portfolio = sharesTransaction.getPortfolio();

        for (UserPortfolio userPortfolio: portfolio.getUserPortfolios()) {
            double userAmount = amount * (userPortfolio.getOwnershipPercentage() / 100);
            userPortfolio.setContributionAmount(userPortfolio.getContributionAmount() + userAmount);
            userPortfolioService.save(userPortfolio);
            userTransactionService.updateShare(userPortfolio, userAmount,
                    "Shares price " + sharesTransaction.getDisplayName() + " Qty " + sharesTransaction.getQty() + " updated "  + " to " + amountToUpdate
                    );
        }

        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Void> sellShare(Long portfolioId, Long shareId, double sellingPrice, int qty, double charges) {
        Portfolio portfolio = portfolioService.findById(portfolioId).orElseThrow(() -> new DataNotFoundException("Portfolio not found"));
        portfolio.updateTotalValue(qty * sellingPrice - charges);

        portfolioService.save(portfolio);
        SharesTransaction sharesTransaction =
                sharesTransactionRepository.findById(shareId).orElseThrow(() -> new DataNotFoundException("SharesTransaction not found"));

        if (sharesTransaction.getQty() < qty)
            throw new DataInconsistentException("Qty should be greater than selling or equal to present qty");

        sharesTransaction.setQty(sharesTransaction.getQty() - qty);
        if (sharesTransaction.getQty() == 0) {
            sharesTransactionRepository.delete(sharesTransaction);
        }
        portfolioTransactionService.sellShares(portfolio, sharesTransaction, qty, sellingPrice, charges);

        for (UserPortfolio userPortfolio: portfolio.getUserPortfolios()) {
            double userAmount = charges * (userPortfolio.getOwnershipPercentage() / 100);
            userPortfolio.setContributionAmount(userPortfolio.getContributionAmount() - userAmount);
            userPortfolioService.save(userPortfolio);
            userTransactionService.updateShare(userPortfolio, -userAmount,
                    "Shares sold charges " + sharesTransaction.getDisplayName() + " at " + charges
                    );
        }

        return ResponseEntity.noContent().build();
    }

    @Transactional
    public SharesTransaction buyShares(
            Long portfolioId,
            double buyingPrice,
            int qty,
            String displayName,
            double charges
    ) {
        Portfolio portfolio = portfolioService.findById(portfolioId).orElseThrow();

        double totalPrice = (buyingPrice * qty) + charges;
        if (portfolio.getTotalValue() < totalPrice) {
            throw new DataInconsistentException("Insufficient funds");
        }

        SharesTransaction sharesTransaction = new SharesTransaction();
        sharesTransaction.setBuyingPrice(buyingPrice);
        sharesTransaction.setCurrentPrice(buyingPrice);
        sharesTransaction.setQty(qty);
        sharesTransaction.setPortfolio(portfolio);
        sharesTransaction.setDisplayName(displayName);
        sharesTransactionRepository.save(sharesTransaction);

        portfolio.setTotalValue(portfolio.getTotalValue() - totalPrice);

        portfolioService.save(portfolio);
        portfolioTransactionService.buyShares(portfolio, sharesTransaction, totalPrice, charges);

        for (UserPortfolio userPortfolio: portfolio.getUserPortfolios()) {
            double userAmount = charges * (userPortfolio.getOwnershipPercentage() / 100);
            userPortfolio.setContributionAmount(userPortfolio.getContributionAmount() - userAmount);
            userPortfolioService.save(userPortfolio);
            userTransactionService.updateShare(userPortfolio, -userAmount,
                    "Shares bought charges " + sharesTransaction.getDisplayName() + " at " + charges
                    );
        }

        return sharesTransaction;
    }

}

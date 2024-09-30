package org.example.fakeportfolios.controller;

import org.example.fakeportfolios.service.SharesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/shares")
public class SharesController {

    private final SharesService sharesService;

    public SharesController(SharesService sharesService) {
        this.sharesService = sharesService;
    }

    @PostMapping("/update-share-price")
    public ResponseEntity<Void> updateSharePrice(@RequestParam Long shareId, @RequestParam double amountToUpdate) {
        return sharesService.updateSharePrice(shareId, amountToUpdate);
    }

    @PostMapping("/{shareId}/sell-share")
    public ResponseEntity<?> sellShares(
            @PathVariable Long shareId,
            @RequestParam double sellingPrice,
            @RequestParam int qty,
            @RequestParam Long portfolioId
    ) {
        sharesService.updateSharePrice(shareId, sellingPrice);
        return sharesService.sellShare(portfolioId, shareId, sellingPrice, qty);
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchShares(@RequestParam("query") String query) {
        try {
            String response = sharesService.search(query);
            return ResponseEntity.ok(response);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching stock data");
        }
    }
}

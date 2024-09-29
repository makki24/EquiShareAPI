package org.example.fakeportfolios.controller;

import org.example.fakeportfolios.service.SharesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/shares")
public class SharesController {

    private final SharesService sharesService;

    public SharesController(SharesService sharesService) {
        this.sharesService = sharesService;
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

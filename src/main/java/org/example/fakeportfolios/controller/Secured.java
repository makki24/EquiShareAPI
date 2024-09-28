package org.example.fakeportfolios.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Secured {

    @GetMapping("/secured")
    public String secured() {
        return "secured";
    }

    @GetMapping("/man")
    @PreAuthorize("hasRole('ADMIN')")
    public String man() {
        return "man";
    }

}

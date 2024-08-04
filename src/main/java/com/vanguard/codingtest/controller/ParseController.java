package com.vanguard.codingtest.controller;

import com.vanguard.codingtest.service.EventStorageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParseController {
    private final EventStorageService eventStorageService;

    public ParseController(EventStorageService eventStorageService) {
        this.eventStorageService = eventStorageService;
    }

    @GetMapping("/v1/index")
    public String index() {
        return "Greetings from Spring Boot index v1!";
    }

    @PostMapping("/v1/setup")
    public void setup() {
        eventStorageService.loadEvents();
    }
}

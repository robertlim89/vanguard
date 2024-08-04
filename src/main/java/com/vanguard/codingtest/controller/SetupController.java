package com.vanguard.codingtest.controller;

import com.vanguard.codingtest.service.EventStorageService;
import org.springframework.web.bind.annotation.*;

@RestController
public class SetupController {
    private final EventStorageService eventStorageService;

    public SetupController(EventStorageService eventStorageService) {
        this.eventStorageService = eventStorageService;
    }

    @GetMapping("/v1/index")
    public String index() {
        return "Greetings from Spring Boot index v1!";
    }

    @PostMapping(value = "/v1/setup")
    public void setup() {
        eventStorageService.loadEvents();
    }
}

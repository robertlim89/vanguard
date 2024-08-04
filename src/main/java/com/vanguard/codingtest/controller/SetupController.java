package com.vanguard.codingtest.controller;

import com.vanguard.codingtest.service.EventStorageService;
import org.springframework.web.bind.annotation.*;

@RestController
public class SetupController {
    private final EventStorageService eventStorageService;

    public SetupController(EventStorageService eventStorageService) {
        this.eventStorageService = eventStorageService;
    }

    @PostMapping(value = "/v1/setup")
    public String setup() {
        return "%s events loaded".formatted(eventStorageService.loadEvents());
    }
}

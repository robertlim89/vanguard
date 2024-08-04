package com.vanguard.codingtest.controller;

import com.vanguard.codingtest.model.Event;
import com.vanguard.codingtest.model.Query;
import com.vanguard.codingtest.service.EventRetrievalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueryController {
    private final EventRetrievalService eventRetrievalService;

    public QueryController(EventRetrievalService eventRetrievalService) {
        this.eventRetrievalService = eventRetrievalService;
    }

    @GetMapping("/v1/query")
    public List<Event> custom(@RequestParam(required = false) String type,
                              @RequestBody(required = false) Query<?> query) {
        return eventRetrievalService.getEvents(type, query);
    }
}

package com.vanguard.codingtest.service;

import com.vanguard.codingtest.model.Event;
import com.vanguard.codingtest.model.Query;
import com.vanguard.codingtest.service.interfaces.IStorageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventRetrievalService {
    private final IStorageService storageService;

    public EventRetrievalService(IStorageService storageService) {
        this.storageService = storageService;
    }

    public List<Event> getEvents(String queryType, Query<?> query) {
        if (queryType == null) return storageService.getAllEvents();
        if (queryType.equalsIgnoreCase("basic")) {
            return storageService.getAllEvents().stream().filter(Query.BASIC_QUERY.evaluate()).toList();
        } else {
            return storageService.getAllEvents().stream().filter(query.evaluate()).toList();
        }
    }
}

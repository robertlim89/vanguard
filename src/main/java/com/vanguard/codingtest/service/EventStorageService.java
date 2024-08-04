package com.vanguard.codingtest.service;

import com.vanguard.codingtest.service.interfaces.IEventService;
import com.vanguard.codingtest.service.interfaces.IStorageService;
import org.springframework.stereotype.Service;

@Service
public class EventStorageService {
    private final IEventService eventService;
    private final IStorageService storageService;

    public EventStorageService(IEventService eventService, IStorageService storageService) {
        this.eventService = eventService;
        this.storageService = storageService;
    }

    public void loadEvents() {
        try {
            var events = eventService.getEvents();
            storageService.storeEvents(events);
        } catch (Exception e) {
            throw e;
        }
    }
}

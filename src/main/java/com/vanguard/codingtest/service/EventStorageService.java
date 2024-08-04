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

    public int loadEvents() {
        var events = eventService.getEvents();
        return storageService.storeEvents(events);
    }
}

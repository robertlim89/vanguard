package com.vanguard.codingtest.service;

import com.vanguard.codingtest.model.ComparisonOperation;
import com.vanguard.codingtest.model.Event;
import com.vanguard.codingtest.model.LogicalOperation;
import com.vanguard.codingtest.model.Query;
import com.vanguard.codingtest.service.interfaces.IEventService;
import com.vanguard.codingtest.service.interfaces.IStorageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventStorageService {
    private final IEventService eventService;
    private final IStorageService storageService;

    private final Query<String> simpleQuery = new Query<>(LogicalOperation.OR,
            List.of(
                    new Query<String>(LogicalOperation.AND, List.of(
                            new Query<>("sellerParty", "EMU_BANK", ComparisonOperation.EQUALS),
                            new Query<>("premiumCurrency", "AUD", ComparisonOperation.EQUALS)
                    )),
                    new Query<String>(LogicalOperation.AND, List.of(
                            new Query<>("buyerParty", "BISON_BANK", ComparisonOperation.EQUALS),
                            new Query<>("premiumCurrency", "USD", ComparisonOperation.EQUALS)
                    ))
            ));

    public EventStorageService(IEventService eventService, IStorageService storageService) {
        this.eventService = eventService;
        this.storageService = storageService;
    }

    public void loadEvents() {
        var events = eventService.getEvents();
        storageService.storeEvents(events);
    }

    public List<Event> getEvents(String queryType, Query<?> query) {
        if (queryType == null) return storageService.getAllEvents();
        if (queryType.equalsIgnoreCase("basic")) {
            return storageService.getAllEvents().stream().filter(simpleQuery.evaluate()).toList();
        } else {
            return storageService.getAllEvents().stream().filter(query.evaluate()).toList();
        }
    }
}

package com.vanguard.codingtest.service.interfaces;

import com.vanguard.codingtest.model.Event;

import java.util.List;

public interface IStorageService {
    int storeEvents(List<Event> events);

    List<Event> getAllEvents();
}

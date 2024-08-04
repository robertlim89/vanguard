package com.vanguard.codingtest.service.interfaces;

import com.vanguard.codingtest.model.Event;

import java.util.List;

public interface IStorageService {
    void storeEvents(List<Event> events);
}

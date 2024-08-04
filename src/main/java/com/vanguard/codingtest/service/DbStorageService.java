package com.vanguard.codingtest.service;

import com.vanguard.codingtest.model.Event;
import com.vanguard.codingtest.service.interfaces.EventRepository;
import com.vanguard.codingtest.service.interfaces.IStorageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbStorageService implements IStorageService {

    private final EventRepository eventRepository;

    public DbStorageService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public int storeEvents(List<Event> events) {
        return eventRepository.saveAll(events).size();
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}

package com.vanguard.codingtest.service;

import com.vanguard.codingtest.model.Event;
import com.vanguard.codingtest.service.interfaces.IStorageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbStorageService implements IStorageService {

    @Override
    public void storeEvents(List<Event> events) {

    }
}

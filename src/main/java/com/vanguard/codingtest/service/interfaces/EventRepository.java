package com.vanguard.codingtest.service.interfaces;

import com.vanguard.codingtest.model.Event;
import org.springframework.data.repository.ListCrudRepository;

public interface EventRepository extends ListCrudRepository<Event, Long> {
}

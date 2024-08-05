package com.vanguard.codingtest.service.interfaces;

import com.vanguard.codingtest.model.Event;
import org.springframework.data.repository.ListCrudRepository;

public interface IEventRepository extends ListCrudRepository<Event, Long> {
}

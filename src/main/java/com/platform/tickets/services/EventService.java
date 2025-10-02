package com.platform.tickets.services;

import com.platform.tickets.domain.CreateEventRequest;
import com.platform.tickets.domain.entities.Event;

import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);
}

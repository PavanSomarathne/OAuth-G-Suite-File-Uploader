package com.example.demo.service;


import java.io.IOException;
import java.util.List;

import com.google.api.services.calendar.model.Event;

import com.example.demo.model.CalendarEvent;

    /**
     * @author vimukthi_r
     *
     */
    public interface CalenderService {

        public List<Event> getLatestEventList() throws IOException;

        public void createNewEvent(CalendarEvent calendarEvent) throws IOException;
    }


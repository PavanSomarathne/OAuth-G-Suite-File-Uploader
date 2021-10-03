package com.example.demo.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import com.example.demo.constant.ApplicationConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;

import com.example.demo.model.CalendarEvent;
import com.example.demo.service.CalenderService;
import com.example.demo.Utils.Utils;

/**
 * @author vimukthi_r
 *
 */
@Service
public class GoogleCalendarService implements CalenderService {

    @Autowired
    AuthorizationServiceImpl authenticationService;

//    @Autowired
//    private HttpTransport httpTransport;

    private Logger logger = LoggerFactory.getLogger(GoogleCalendarService.class);
    private Calendar service = null;

    @PostConstruct
    public void init() throws IOException {
        this.service = new Calendar.Builder(ApplicationConstant.HTTP_TRANSPORT, Utils.JSON_FACTORY,
                authenticationService.getCredentials()).setApplicationName(ApplicationConstant.APPLICATION_NAME).build();
    }

    public List<Event> getLatestEventList() throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());

        // Sends the metadata request to the resource server and returns the parsed
        // metadata response.
        Events events = service.events().list("primary").setMaxResults(10).setTimeMin(now).setOrderBy("startTime")
                .setSingleEvents(true).execute();

        // Retrieve events list
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            logger.info("No upcoming events found.");
        } else {
            logger.info("Upcoming events found.");
            items.forEach(e -> logger.info("Event : {} {} {} {}", e.getSummary(), e.getDescription(), e.getHtmlLink(),
                    e.getStart().getDateTime()));
        }
        return items;
    }

    // Create a new event and sends the request to the auth end point
    public void createNewEvent(CalendarEvent calendarEvent) throws IOException {
        // Initialize a new event
        Event event = new Event().setSummary(calendarEvent.getTitle()).setLocation(calendarEvent.getLocation())
                .setDescription(calendarEvent.getDescription());

        // Add start date
        DateTime startDateTime = new DateTime(String.format(Utils.RFC_3339_DATETIME_FORMAT,
                calendarEvent.getDate(), calendarEvent.getStartTime()));
        EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone(Utils.UTC_SL_TIME_ZONE);
        event.setStart(start);

        // Add end date
        DateTime endDateTime = new DateTime(String.format(Utils.RFC_3339_DATETIME_FORMAT, calendarEvent.getDate(),
                calendarEvent.getEndTime()));
        EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone(Utils.UTC_SL_TIME_ZONE);
        event.setEnd(end);

        this.service.events().insert("primary", event).execute();
    }

}
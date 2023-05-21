package com.example.interactiondesigngroup19.ui.calendar;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.interactiondesigngroup19.ui.util.Indicator;
import com.example.interactiondesigngroup19.ui.util.IndicatorHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Map;

public class CalendarEventHandler {

    private final LinkedList<CalendarEvent> events;
    private final EventWriteReader eventReadWriter;
    private final Map<LocalDate, Integer> dateCounts;

    public CalendarEventHandler(Context context) {
        eventReadWriter = new EventWriteReader(context);
        events = eventReadWriter.ReadEvents();
        dateCounts = new HashMap<>();
        initializeEventCounts();
    }

    private void initializeEventCounts() {
        for (CalendarEvent event :
                events) {
            dateCounts.merge(event.getStartDateTime().toLocalDate(), 1, Integer::sum);
        }
    }

    public void addEvent(CalendarEvent event) {
        addSortedEvent(event);
        eventReadWriter.WriteEvents(events);
    }

    public void addEvent(LocalDateTime startDateTime, LocalTime endTime, List<Indicator> indicators) {
        addSortedEvent(new CalendarEvent(startDateTime, endTime, indicators));
        eventReadWriter.WriteEvents(events);
    }

    public void addEvent(LocalDateTime startDateTime, LocalTime endTime, List<Indicator> indicators, String note) {
        addSortedEvent(new CalendarEvent(startDateTime, endTime, indicators, note));
        eventReadWriter.WriteEvents(events);
    }

    public List<CalendarEvent> getEvents() {
        return events;
    }

    private void addSortedEvent(CalendarEvent event) {
        dateCounts.merge(event.getStartDateTime().toLocalDate(), 1, Integer::sum);
        LocalDateTime eventStartTime = event.getStartDateTime();

        if (events.size() == 0) {
            events.add(event);
        } else if (events.getFirst().getStartDateTime().compareTo(eventStartTime) > 0) {
            events.addFirst(event);
        } else if (events.getLast().getStartDateTime().compareTo(eventStartTime) < 0) {
            events.addLast(event);
        } else {
            Iterator<CalendarEvent> eventIterator = events.iterator();
            int idx = 0;
            CalendarEvent current = eventIterator.next();
            while (current.getStartDateTime().compareTo(eventStartTime) < 0) {
                idx++;
                current = eventIterator.next();
            }
            events.add(idx, event);
        }
    }

    public CalendarEvent addRandomEvent() {
        Random rand = new Random();
        LocalDateTime randomStart = LocalDateTime.of(2023, 1 + rand.nextInt(2), 1 + rand.nextInt(2), rand.nextInt(24), rand.nextInt(60));
        LocalTime randomEnd = LocalTime.of(rand.nextInt(24), rand.nextInt(60));

        CalendarEvent calendarEvent = new CalendarEvent(randomStart, randomEnd, IndicatorHelper.fromArray(new boolean[] {true, false, true, false}));
        addEvent(calendarEvent);

        return calendarEvent;
    }

    public void removeEvent(CalendarEvent event) {
        events.remove(event);
        dateCounts.merge(event.getStartDateTime().toLocalDate(), -1, Integer::sum);
        eventReadWriter.WriteEvents(events);
        System.out.println(dateCounts);
    }

    public boolean eventsWithSameDate(CalendarEvent event) {
        // returns whether other calendar events share the same date as `event`
        return dateCounts.get(event.getStartDateTime().toLocalDate()) > 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "CalendarEventHandler{" +
                "events=" + events +
                '}';
    }
}

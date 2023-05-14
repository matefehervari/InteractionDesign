package com.example.interactiondesigngroup19.ui.calendar;

import java.time.LocalTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CalendarEventHandler {
    private final LinkedList<CalendarEvent> events;

    public CalendarEventHandler() {
        events = new LinkedList<>();
    }

    public void addEvent(CalendarEvent event) {
        addSortedEvent(event);
    }

    public void addEvent(LocalTime startTime, LocalTime endTime, List<IndicatorsStud> indicators) {
        addSortedEvent(new CalendarEvent(startTime, endTime, indicators));
    }

    public List<CalendarEvent> getEvents() {
        return events;
    }

    private void addSortedEvent(CalendarEvent event) {
        LocalTime eventStartTime = event.getStartTime();

        if (events.size() == 0) {
            events.add(event);
        }
        else if (events.getFirst().getStartTime().compareTo(eventStartTime) > 0) {
            events.addFirst(event);
        }
        else if (events.getLast().getStartTime().compareTo(eventStartTime) < 0) {
            events.addLast(event);
        }
        else {
            Iterator<CalendarEvent> eventIterator = events.iterator();
            int idx = 0;
            CalendarEvent current = eventIterator.next();
            while (current.getStartTime().compareTo(eventStartTime) < 0) {
                idx++;
                current = eventIterator.next();
            }
            events.add(idx, event);
        }
    }

    public void addRandomEvent() {
        Random rand = new Random();
        LocalTime randomStart = LocalTime.of(rand.nextInt(24), rand.nextInt(60));
        LocalTime randomEnd = LocalTime.of(rand.nextInt(24), rand.nextInt(60));

        addSortedEvent(new CalendarEvent(randomStart, randomEnd, new LinkedList<IndicatorsStud>()));
    }

    @Override
    public String toString() {
        return "CalendarEventHandler{" +
                "events=" + events +
                '}';
    }
}

package com.example.interactiondesigngroup19.ui.calendar;

import java.time.LocalTime;
import java.util.List;

public class CalendarEvent {

    private final LocalTime startTime;
    private final LocalTime endTime;

    private final List<IndicatorsStud> indicators;

    public CalendarEvent(LocalTime startTime, LocalTime endTime, List<IndicatorsStud> indicators) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.indicators = indicators;
    }

    public String getStartTimeString() {
        return "" + startTime.getHour() + ":" + startTime.getMinute();
    }

    public String getEndTimeString() {
        return "" + endTime.getHour() + ":" + endTime.getMinute();
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public List<IndicatorsStud> getIndicators() {
        return indicators;
    }

    @Override
    public String toString() {
        return "CalendarEvent{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

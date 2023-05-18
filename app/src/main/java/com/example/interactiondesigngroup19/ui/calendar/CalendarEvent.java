package com.example.interactiondesigngroup19.ui.calendar;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

public class CalendarEvent implements Serializable {

    private final LocalTime startTime;
    private final LocalTime endTime;

    private final List<IndicatorsStud> indicators;

    public CalendarEvent(LocalTime startTime, LocalTime endTime, List<IndicatorsStud> indicators) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.indicators = indicators;
    }

    public String getStartTimeString() {
        return "" + padZero(2, startTime.getHour()) + ":" + padZero(2, startTime.getMinute());
    }

    public String getEndTimeString() {
        return "" + padZero(2, endTime.getHour()) + ":" + padZero(2, endTime.getMinute());
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

    private String padZero(int length, String string) {
        if (string.length() >= length) {
            return string;
        }
        else {
            return "0".repeat(length - string.length()) + string;
        }
    }

    private String padZero(int length, int integer) {
        return padZero(length, String.valueOf(integer));
    }

    @Override
    public String toString() {
        return "CalendarEvent{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

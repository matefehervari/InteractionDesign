package com.example.interactiondesigngroup19.ui.calendar;

import androidx.annotation.NonNull;

import com.example.interactiondesigngroup19.ui.util.Indicator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

public class CalendarEvent implements Serializable {

    private final LocalDateTime startDateTime;
    private final LocalTime endTime;

    private final List<Indicator> indicators;

    public CalendarEvent(LocalDateTime startTime, LocalTime endTime, List<Indicator> indicators) {
        this.startDateTime = startTime;
        this.endTime = endTime;
        this.indicators = indicators;
    }

    public String getStartTimeString() {
        return "" + padZero(startDateTime.getHour()) + ":" + padZero(startDateTime.getMinute());
    }

    public String getEndTimeString() {
        return "" + padZero(endTime.getHour()) + ":" + padZero(endTime.getMinute());
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public List<Indicator> getIndicators() {
        return indicators;
    }

    private String padZero(int integer) {
        return String.format(Locale.ENGLISH,"%02d", integer);
    }

    @NonNull
    @Override
    public String toString() {
        return "CalendarEvent{" +
                "startTime=" + startDateTime +
                ", endTime=" + endTime +
                '}';
    }
}

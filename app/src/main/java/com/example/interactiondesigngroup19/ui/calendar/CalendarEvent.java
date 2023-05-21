package com.example.interactiondesigngroup19.ui.calendar;

import androidx.annotation.NonNull;

import com.example.interactiondesigngroup19.ui.util.Indicator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

public class CalendarEvent implements Serializable {
    public static final int NO_DATE_ID = -1;
    private final LocalDateTime startDateTime;
    private final LocalTime endTime;
    private final List<Indicator> indicators;
    private final String note;
    private int dateId;

    public CalendarEvent(LocalDateTime startTime, LocalTime endTime, List<Indicator> indicators) {
        this.startDateTime = startTime;
        this.endTime = endTime;
        this.indicators = indicators;
        this.note = "Calendar Event";
    }

    public CalendarEvent(LocalDateTime startTime, LocalTime endTime, List<Indicator> indicators, String note) {
        this.startDateTime = startTime;
        this.endTime = endTime;
        this.indicators = indicators;
        this.note = note;
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

    public String getNote() { return note; }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public int getDateId() {
        return dateId;
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

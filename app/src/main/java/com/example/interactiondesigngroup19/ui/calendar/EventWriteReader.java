package com.example.interactiondesigngroup19.ui.calendar;

import android.content.Context;

import com.example.interactiondesigngroup19.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class EventWriteReader {

    private final Context context;
    private final String eventsFileName;
    private final String pathToFile;

    public EventWriteReader(Context context) {
        this.context = context;
        this.eventsFileName = context.getResources().getString(R.string.events_file);
        this.pathToFile = Paths.get(context.getFilesDir().getPath(), eventsFileName).toString();

        File eventFile = new File(pathToFile);
    }

    public void WriteEvents(List<CalendarEvent> events) {
        try {
            File eventFile = new File(pathToFile);
            eventFile.createNewFile();

            FileOutputStream f = new FileOutputStream(eventFile);
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(events);

            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("Events file not found");
        } catch (IOException e) {
            System.out.println("Error initialising stream");
        }
    }

    @SuppressWarnings("unchecked")
    public LinkedList<CalendarEvent> ReadEvents() {
        try {
            File file = new File(pathToFile);

            if (file.length() != 0) {
                FileInputStream fi = new FileInputStream(file);
                ObjectInputStream oi = new ObjectInputStream(fi);

                LinkedList<CalendarEvent> events = (LinkedList<CalendarEvent>) oi.readObject();

                fi.close();
                oi.close();

                return events;
            } else {
                System.out.println("events file empty");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Events file not found");
        } catch (IOException e) {
            System.out.println("Error initialising stream");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new LinkedList<>();
    }
}
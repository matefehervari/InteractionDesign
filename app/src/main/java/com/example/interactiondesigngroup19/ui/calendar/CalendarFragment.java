package com.example.interactiondesigngroup19.ui.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.interactiondesigngroup19.databinding.FragmentCalendarBinding;
import com.google.android.material.card.MaterialCardView;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;
    private CalendarEventHandler eventHandler;
    private LinearLayout fragmentLinearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CalendarViewModel calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        eventHandler = new CalendarEventHandler(this.getContext());
        fragmentLinearLayout = binding.calendarLinearLayout;

        for (CalendarEvent event :
                eventHandler.getEvents()) {
            fragmentLinearLayout.addView(createEventCard(event));
        }
        onViewCreated(this.getView(), savedInstanceState);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button testButton = binding.button;
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarEvent calendarEvent = eventHandler.addRandomEvent();
//                System.out.println(eventHandler);
                fragmentLinearLayout.addView(createEventCard(calendarEvent));
            }
        });
    }

    public MaterialCardView createEventCard(CalendarEvent event) {
        MaterialCardView cv = new MaterialCardView(this.getContext());

        // card view
        LinearLayout.LayoutParams cvLayoutParams = new LinearLayout.LayoutParams(-1, -1); // match parent
        cvLayoutParams.setMargins(15,15,15,15);

        cv.setLayoutParams(cvLayoutParams);
        cv.setClickable(true);
        cv.setFocusable(true);
        cv.setCheckable(true);
        cv.setStrokeColor(Color.BLACK);
        cv.setStrokeWidth(2);
        cv.setRadius(15);

        // linear layout inside card layout
        LinearLayout cardLayout = new LinearLayout(this.getContext());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(-1, -2); // match parent, wrap content
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.setLayoutParams(linearLayoutParams);
        cardLayout.setPadding(16,16,16,16);

        // text
        TextView cv_t = new TextView(this.getContext());
        cv_t.setText(event.getStartTimeString());

        TextView cv_t2 = new TextView(this.getContext());
        cv_t2.setText(event.getEndTimeString());

        cardLayout.addView(cv_t);
        cardLayout.addView(cv_t2);
        cv.addView(cardLayout);

        return cv;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
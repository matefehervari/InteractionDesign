package com.example.interactiondesigngroup19.ui.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.interactiondesigngroup19.R;
import com.example.interactiondesigngroup19.databinding.FragmentCalendarBinding;
import com.google.android.material.card.MaterialCardView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

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

        createEventCards();

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

    private void createEventCards() {
        LocalDate lastDate = null;
        for (CalendarEvent event :
                eventHandler.getEvents()) {
            LocalDate eventDate = event.getStartDateTime().toLocalDate();
            if (!eventDate.equals(lastDate)) {

                TextView dateTextView = new TextView(this.getContext());
                dateTextView.setText(eventDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.UK)));
                LinearLayout.LayoutParams dateTextLayout = new LinearLayout.LayoutParams(-1, -1); // match-parent, match-parent
                dateTextLayout.setMargins(15, 0, 0, 0);
                dateTextView.setLayoutParams(dateTextLayout);
                dateTextView.setTextSize(20);

                fragmentLinearLayout.addView(dateTextView);

                lastDate = eventDate;
            }
            fragmentLinearLayout.addView(createEventCard(event));
        }
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
        cv.setStrokeColor(Color.GRAY);
        cv.setStrokeWidth(2);
        cv.setRadius(15);

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewManager) v.getParent()).removeView(v);
                eventHandler.removeEvent(event);
            }
        });

        // Horizontal layout inside card layout
        LinearLayout cardLayout = new LinearLayout(this.getContext());
        cardLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(-1, -2); // match_parent, wrap_content
        cardLayout.setLayoutParams(cardLayoutParams);

        // Vertical layout for text
        LinearLayout textLayout = new LinearLayout(this.getContext());
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(-2, -2); // wrap_content, wrap_content
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setLayoutParams(textLayoutParams);
        textLayout.setPadding(16,16,16,16);

        // text
        TextView note = new TextView(this.getContext());
        LinearLayout.LayoutParams noteParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        note.setLayoutParams(noteParams);
        note.setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Headline6);
        note.setText("Note");

        TextView startTime = new TextView(this.getContext());
        startTime.setTextSize(15);
        startTime.setText(event.getStartTimeString());
        LinearLayout.LayoutParams startTimeLayout = new LinearLayout.LayoutParams(-2, -2); // wrap_content
        startTimeLayout.setMargins(0, 8, 0, 0);
        startTime.setLayoutParams(startTimeLayout);

        TextView endTime = new TextView(this.getContext());
        endTime.setTextSize(15);
        endTime.setText(event.getEndTimeString());
        LinearLayout.LayoutParams endTimeLayout = new LinearLayout.LayoutParams(-2, -2); // wrap_content
        endTimeLayout.setMargins(0, 16, 0, 0);
        endTime.setLayoutParams(endTimeLayout);

        textLayout.addView(note);
        textLayout.addView(startTime);
        textLayout.addView(endTime);

        // Relative Layout for indicators
        RelativeLayout indicatorLayout = new RelativeLayout(this.getContext());
        RelativeLayout.LayoutParams indicatorLayoutParams = new RelativeLayout.LayoutParams(-2, -1); // wrap_content, match_parent
        indicatorLayoutParams.setMargins(50,50,20,50);
        indicatorLayout.setLayoutParams(indicatorLayoutParams);
        indicatorLayout.setPadding(10, 10, 10, 10);

        // Light
        ImageView lightIndicator = new ImageView(this.getContext());
        RelativeLayout.LayoutParams lightParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lightIndicator.setLayoutParams(lightParams);
        lightIndicator.setImageDrawable(this.getContext().getDrawable(R.drawable.light_indicator_icon));
        lightIndicator.setAdjustViewBounds(true);
        int lightId = View.generateViewId();
        lightIndicator.setId(lightId);

        indicatorLayout.addView(lightIndicator);

        // Coat
        ImageView coatIndicator = new ImageView(this.getContext());
        RelativeLayout.LayoutParams coatParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        coatParams.addRule(RelativeLayout.LEFT_OF, lightId);
        coatIndicator.setLayoutParams(coatParams);
        coatIndicator.setImageDrawable(this.getContext().getDrawable(R.drawable.coat_indicator_icon));
        coatIndicator.setAdjustViewBounds(true);
        int coatId = View.generateViewId();
        coatIndicator.setId(coatId);

        indicatorLayout.addView(coatIndicator);

        // Wind
        ImageView windIndicator = new ImageView(this.getContext());
        RelativeLayout.LayoutParams windParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        windParams.addRule(RelativeLayout.LEFT_OF, coatId);
        windIndicator.setLayoutParams(windParams);
        windIndicator.setImageDrawable(this.getContext().getDrawable(R.drawable.wind_indicator_icon));
        windIndicator.setAdjustViewBounds(true);
        int windId = View.generateViewId();
        windIndicator.setId(windId);

        indicatorLayout.addView(windIndicator);

        // Rain
        ImageView rainIndicator = new ImageView(this.getContext());
        RelativeLayout.LayoutParams rainParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        rainParams.addRule(RelativeLayout.LEFT_OF, windId);
        rainIndicator.setLayoutParams(rainParams);
        rainIndicator.setImageDrawable(this.getContext().getDrawable(R.drawable.rain_indicator_icon));
        rainIndicator.setAdjustViewBounds(true);
        indicatorLayout.addView(rainIndicator);

        cardLayout.addView(textLayout);
        cardLayout.addView(indicatorLayout);

        cv.addView(cardLayout);

        return cv;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
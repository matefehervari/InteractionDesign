package com.example.interactiondesigngroup19.ui.route_planner;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.interactiondesigngroup19.R;
import com.example.interactiondesigngroup19.databinding.FragmentRoutePlannerBinding;
import com.example.interactiondesigngroup19.ui.calendar.CalendarEvent;
import com.example.interactiondesigngroup19.ui.calendar.CalendarEventHandler;
import com.example.interactiondesigngroup19.ui.home.HomeViewModel;
import com.example.interactiondesigngroup19.ui.route_planner.RoutePlannerViewModel;
import com.example.interactiondesigngroup19.ui.route_planner.RoutePlannerViewModel;
import com.example.interactiondesigngroup19.ui.route_planner.RoutePlannerViewModel;
import com.example.interactiondesigngroup19.ui.util.Indicator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RoutePlannerFragment extends Fragment {

    private FragmentRoutePlannerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRoutePlannerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        onViewCreated(this.getView(), savedInstanceState);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Spinner hourSpinner = binding.HourSpinner;
        final Spinner minuteSpinner = binding.MinuteSpinner;
        final Spinner dateSpinner = binding.DateSpinner;
        final Spinner endHourSpinner = binding.endHourSpinner;
        final Spinner endMinuteSpinner = binding.endMinuteSpinner;

        final EditText notesEditText = binding.notesEditText;

        final ImageButton submitButton = binding.submitEventButton;
        final ImageButton calendarButton = binding.calendarButton;

        final ImageView rainIndicatorImage = binding.rainIndicator;
        final ImageView windIndicatorImage = binding.windIndicator;
        final ImageView coatIndicatorImage = binding.coatIndicator;
        final ImageView lightIndicatorImage = binding.lightIndicator;

        Integer[] hourList = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};

        Integer[] minuteList = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                                    20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
                                    40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59};

        ArrayAdapter<Integer> adapter1 = new ArrayAdapter<Integer>(this.getContext(), android.R.layout.simple_spinner_item, hourList);
        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(this.getContext(), android.R.layout.simple_spinner_item, minuteList);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hourSpinner.setAdapter(adapter1);
        minuteSpinner.setAdapter(adapter2);

        Calendar calendar = Calendar.getInstance();
        Date day1 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day2 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day3 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day4 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day5 = calendar.getTime();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String day1AsString = dateFormat.format(day1);
        String day2AsString = dateFormat.format(day2);
        String day3AsString = dateFormat.format(day3);
        String day4AsString = dateFormat.format(day4);
        String day5AsString = dateFormat.format(day5);

        String[] dateList = new String[]{day1AsString, day2AsString, day3AsString, day4AsString, day5AsString};

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, dateList);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dateSpinner.setAdapter(adapter3);

        endHourSpinner.setAdapter(adapter1);
        endMinuteSpinner.setAdapter(adapter2);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startHour = hourSpinner.getSelectedItem().toString();
                String startMinute = minuteSpinner.getSelectedItem().toString();
                String[] date = dateSpinner.getSelectedItem().toString().split("/");
                Integer day = Integer.parseInt(date[0]);
                Integer month = Integer.parseInt(date[1]);
                Integer year = Integer.parseInt(date[2]);

                LocalTime startTime = LocalTime.of(Integer.parseInt(startHour), Integer.parseInt(startMinute), 0);

                // Implement check for icons here.

                /*
                RoutePlannerViewModel model = new RoutePlannerViewModel(getActivity().getApplication());
                model.callAPI(getActivity().getApplication());

                rainIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), model.getRainTint()));
                windIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), model.getWindTint()));
                coatIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), model.getCoatTint()));
                lightIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), model.getLightTint()));

                rainIndicatorImage.setScaleX(model.getRainScale());
                rainIndicatorImage.setScaleY(model.getRainScale());
                windIndicatorImage.setScaleX(model.getWindScale());
                windIndicatorImage.setScaleY(model.getWindScale());
                coatIndicatorImage.setScaleX(model.getCoatScale());
                coatIndicatorImage.setScaleY(model.getCoatScale());
                lightIndicatorImage.setScaleX(model.getLightScale());
                lightIndicatorImage.setScaleY(model.getLightScale());
                */

            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                RoutePlannerViewModel model = new RoutePlannerViewModel(getActivity().getApplication());
                model.callAPI(getActivity().getApplication());

                // Indicator Icons
                rainIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), model.getRainTint()));
                windIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), model.getWindTint()));
                coatIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), model.getCoatTint()));
                lightIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), model.getLightTint()));

                rainIndicatorImage.setScaleX(model.getRainScale());
                rainIndicatorImage.setScaleY(model.getRainScale());
                windIndicatorImage.setScaleX(model.getWindScale());
                windIndicatorImage.setScaleY(model.getWindScale());
                coatIndicatorImage.setScaleX(model.getCoatScale());
                coatIndicatorImage.setScaleY(model.getCoatScale());
                lightIndicatorImage.setScaleX(model.getLightScale());
                lightIndicatorImage.setScaleY(model.getLightScale());
                */

                // Get data and send off to calendar

                String startHour = hourSpinner.getSelectedItem().toString();
                String startMinute = minuteSpinner.getSelectedItem().toString();
                String[] date = dateSpinner.getSelectedItem().toString().split("/");
                Integer day = Integer.parseInt(date[0]);
                Integer month = Integer.parseInt(date[1]);
                Integer year = Integer.parseInt(date[2]);

                LocalTime startTime = LocalTime.of(Integer.parseInt(startHour), Integer.parseInt(startMinute), 0);

                String endHour = endHourSpinner.getSelectedItem().toString();
                String endMinute = endMinuteSpinner.getSelectedItem().toString();

                LocalTime endTime = LocalTime.of(Integer.parseInt(endHour), Integer.parseInt(endMinute), 0);

                String notes = notesEditText.getText().toString();

                LocalDate startDate = LocalDate.of(year, month, day);

                LocalDateTime startDateTime = LocalDateTime.of(startDate ,startTime);

                ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
                long epoch = startDateTime.atZone(zoneId).toEpochSecond();

                notesEditText.getText().clear();

                CalendarEventHandler eventHandler = new CalendarEventHandler(getContext());
                eventHandler.addEvent(startDateTime, endTime, new LinkedList<Indicator>());

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
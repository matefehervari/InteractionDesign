package com.example.interactiondesigngroup19.ui.route_planner;

import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.GregorianCalendar;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.interactiondesigngroup19.apis.IndicatorResults;
import com.example.interactiondesigngroup19.apis.LocationAPI;
import com.example.interactiondesigngroup19.apis.WebResourceAPI;
import com.example.interactiondesigngroup19.databinding.FragmentRoutePlannerBinding;
import com.example.interactiondesigngroup19.ui.calendar.CalendarEventHandler;
import com.example.interactiondesigngroup19.ui.util.Indicator;
import com.example.interactiondesigngroup19.ui.util.IndicatorHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

public class RoutePlannerFragment extends Fragment {
    private Button startTimeButton;
    private Button endTimeButton;
    private FragmentRoutePlannerBinding binding;
    private RoutePlannerViewModel routeModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRoutePlannerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        onViewCreated(this.getView(), savedInstanceState);

        LocationAPI.ensureLocationClientAndRequester(getActivity());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        final Spinner hourSpinner = binding.HourSpinner;
//        final Spinner minuteSpinner = binding.MinuteSpinner;
        final Spinner dateSpinner = binding.DateSpinner;
//        final Spinner endHourSpinner = binding.endHourSpinner;
//        final Spinner endMinuteSpinner = binding.endMinuteSpinner;
        startTimeButton = binding.startTimeButton;
        endTimeButton = binding.endTimeButton;

        final EditText notesEditText = binding.notesEditText;

        final EditText startEditText = binding.startEditText;
        final EditText endEditText = binding.endEditText;

        final ImageButton startSearchButton = binding.startSearchButton;
        final ImageButton endSearchButton = binding.endSearchButton;

        final Spinner startSpinner = binding.startSpinner;
        final Spinner endSpinner = binding.endSpinner;

        final ImageButton submitButton = binding.submitEventButton;
        final ImageButton calendarButton = binding.calendarButton;

        final ImageView rainIndicatorImage = binding.rainIndicator;
        final ImageView windIndicatorImage = binding.windIndicator;
        final ImageView coatIndicatorImage = binding.coatIndicator;
        final ImageView lightIndicatorImage = binding.lightIndicator;

        final Map<String, WebResourceAPI.MapLocation> startMap = new HashMap<>();
        final Map<String, WebResourceAPI.MapLocation> endMap = new HashMap<>();

        final RoutePlannerViewModel viewModel = new RoutePlannerViewModel(getActivity().getApplication());

        viewModel.getWeatherUI().observe(getViewLifecycleOwner(), weatherUIData -> {

            // Changes the attributes of the indicator images to indicate on/off
            Context currContext = getActivity().getApplication().getApplicationContext();

            rainIndicatorImage.setColorFilter(ContextCompat.getColor(currContext, weatherUIData.rainTint));
            windIndicatorImage.setColorFilter(ContextCompat.getColor(currContext, weatherUIData.windTint));
            coatIndicatorImage.setColorFilter(ContextCompat.getColor(currContext, weatherUIData.coatTint));
            lightIndicatorImage.setColorFilter(ContextCompat.getColor(currContext, weatherUIData.lightTint));

            rainIndicatorImage.setScaleX(viewModel.getRainScale());
            rainIndicatorImage.setScaleY(viewModel.getRainScale());
            windIndicatorImage.setScaleX(viewModel.getWindScale());
            windIndicatorImage.setScaleY(viewModel.getWindScale());
            coatIndicatorImage.setScaleX(viewModel.getCoatScale());
            coatIndicatorImage.setScaleY(viewModel.getCoatScale());
            lightIndicatorImage.setScaleX(viewModel.getLightScale());
            lightIndicatorImage.setScaleY(viewModel.getLightScale());
        });

        routeModel = new RoutePlannerViewModel(getActivity().getApplication());

        Integer[] hourList = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};

        Integer[] minuteList = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                                    20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
                                    40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59};

        ArrayAdapter<Integer> adapter1 = new ArrayAdapter<Integer>(this.getContext(), android.R.layout.simple_spinner_item, hourList);
        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(this.getContext(), android.R.layout.simple_spinner_item, minuteList);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        hourSpinner.setAdapter(adapter1);
//        minuteSpinner.setAdapter(adapter2);

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

//        endHourSpinner.setAdapter(adapter1);
//        endMinuteSpinner.setAdapter(adapter2);

        String[] emptyLocList = new String[]{""};
        ArrayAdapter<String> startSpinnerAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, emptyLocList);
        startSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startSpinner.setAdapter(startSpinnerAdapter);
        ArrayAdapter<String> endSpinnerAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, emptyLocList);
        endSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endSpinner.setAdapter(endSpinnerAdapter);

        // binds time picker popups
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPopTimePicker(v);
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endPopTimePicker(v);
            }
        });

        startSearchButton.setOnClickListener(view1 -> {
            String search = startEditText.getText().toString();
            String endName = endSpinner.getSelectedItem().toString();

            ArrayAdapter<String> startLoadAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{"Loading..."});
            startLoadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            startSpinner.setAdapter(startLoadAdapter);

            if (endMap.containsKey(endName)) {
                WebResourceAPI.listSearchLocation(getContext(), endMap.get(endName).lat, endMap.get(endName).lon, search, 5, resultList -> {
                    String[] nameList = new String[resultList.size()];
                    startMap.clear();
                    for (int i = 0; i < resultList.size(); i++) {
                        nameList[i] = resultList.get(i).addressLabel;
                        startMap.put(resultList.get(i).addressLabel, resultList.get(i));
                    }
                    ArrayAdapter<String> startNameAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nameList);
                    startNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    startSpinner.setAdapter(startNameAdapter);
                }, e -> {
                    startEditText.getText().clear();
                });
            } else {
                LocationAPI.requestLocation(getActivity(), location ->
                        WebResourceAPI.listSearchLocation(getContext(), location, search, 5, resultList -> {
                            String[] nameList = new String[resultList.size()];
                            startMap.clear();
                            for (int i = 0; i < resultList.size(); i++) {
                                nameList[i] = resultList.get(i).addressLabel;
                                startMap.put(resultList.get(i).addressLabel, resultList.get(i));
                            }
                            ArrayAdapter<String> startNameAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nameList);
                            startNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            startSpinner.setAdapter(startNameAdapter);
                        }, e -> {
                            startEditText.getText().clear();
                        }), e -> {
                    startEditText.getText().clear();
                });
            }

        });

        endSearchButton.setOnClickListener(view1 -> {
            String search = endEditText.getText().toString();
            String startName = startSpinner.getSelectedItem().toString();

            ArrayAdapter<String> endLoadAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{"Loading..."});
            endLoadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            endSpinner.setAdapter(endLoadAdapter);

            if (startMap.containsKey(startName)) {
                WebResourceAPI.listSearchLocation(getContext(), startMap.get(startName).lat, startMap.get(startName).lon, search, 5, resultList -> {
                    String[] nameList = new String[resultList.size()];
                    endMap.clear();
                    for (int i = 0; i < resultList.size(); i++) {
                        nameList[i] = resultList.get(i).addressLabel;
                        endMap.put(resultList.get(i).addressLabel, resultList.get(i));
                    }
                    ArrayAdapter<String> endNameAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nameList);
                    endNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    endSpinner.setAdapter(endNameAdapter);
                }, e -> {
                    endEditText.getText().clear();
                });
            } else {
                LocationAPI.requestLocation(getActivity(), location ->
                        WebResourceAPI.listSearchLocation(getContext(), location, search, 5, resultList -> {
                            String[] nameList = new String[resultList.size()];
                            endMap.clear();
                            for (int i = 0; i < resultList.size(); i++) {
                                nameList[i] = resultList.get(i).addressLabel;
                                endMap.put(resultList.get(i).addressLabel, resultList.get(i));
                            }
                            ArrayAdapter<String> endNameAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nameList);
                            endNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            endSpinner.setAdapter(endNameAdapter);
                        }, e -> {
                            endEditText.getText().clear();
                        }), e -> {
                    endEditText.getText().clear();
                });
            }
        });

        submitButton.setOnClickListener(view1 -> {
            // gets the time params from the viewModel. Left it as string as it was before just in case?
            String startHour = String.valueOf(routeModel.getStartHour());
            String startMinute = String.valueOf(routeModel.getEndHour());

            String[] date = dateSpinner.getSelectedItem().toString().split("/");
            Integer day = Integer.parseInt(date[0]);
            Integer month = Integer.parseInt(date[1]);
            Integer year = Integer.parseInt(date[2]);

            GregorianCalendar startCal = new GregorianCalendar();
            TimeZone tz = TimeZone.getDefault();
            startCal.set(year, month, day, Integer.parseInt(startHour), Integer.parseInt(startMinute), 0);
            startCal.set(Calendar.ZONE_OFFSET, tz.getOffset(startCal.getTimeInMillis()));

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

        });

        final CalendarEventHandler eventHandler = new CalendarEventHandler(getContext());
        calendarButton.setOnClickListener(view1 -> {

            // Get data and send off to calendar

            String startHour = String.valueOf(routeModel.getStartHour());
            String startMinute = String.valueOf(routeModel.getStartMin());
            String[] date = dateSpinner.getSelectedItem().toString().split("/");
            Integer day = Integer.parseInt(date[0]);
            Integer month = Integer.parseInt(date[1]);
            Integer year = Integer.parseInt(date[2]);

            LocalTime startTime = LocalTime.of(Integer.parseInt(startHour), Integer.parseInt(startMinute), 0);

            String endHour = String.valueOf(routeModel.getEndHour());
            String endMinute = String.valueOf(routeModel.getEndMin());

            LocalTime endTime = LocalTime.of(Integer.parseInt(endHour), Integer.parseInt(endMinute), 0);

            String notes = notesEditText.getText().toString();

            LocalDate startDate = LocalDate.of(year, month, day);

            LocalDateTime startDateTime = LocalDateTime.of(startDate ,startTime);

            ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
            long epoch = startDateTime.atZone(zoneId).toEpochSecond() * 1000;

            notesEditText.getText().clear();

            String startName = startSpinner.getSelectedItem().toString();
            String endName = endSpinner.getSelectedItem().toString();

            if(startMap.containsKey(startName) && endMap.containsKey(endName)) {
                WebResourceAPI.MapLocation startLoc = startMap.get(startName);
                WebResourceAPI.MapLocation endLoc = endMap.get(endName);
                WebResourceAPI.getWeatherForecast(getContext(), startLoc.lat, startLoc.lon, epoch, res1 -> {
                    WebResourceAPI.getWeatherForecast(getContext(), endLoc.lat, endLoc.lon, epoch, res2 -> {
                        boolean[] startResults = IndicatorResults.indicatorResults(res1);
                        boolean[] endResults = IndicatorResults.indicatorResults(res2);
                        for (int i = 0; i < 4; i++) {
                            startResults[i] |= endResults[i];
                        }
                        viewModel.processWeatherIndicators(startResults);
                        eventHandler.addEvent(startDateTime, endTime, IndicatorHelper.fromArray(startResults), notes,
                                startLoc, endLoc);
                    }, e -> {
                        boolean[] startResults = IndicatorResults.indicatorResults(res1);
                        viewModel.processWeatherIndicators(startResults);
                        eventHandler.addEvent(startDateTime, endTime, IndicatorHelper.fromArray(startResults), notes,
                            startLoc, endLoc);});
                }, e -> {eventHandler.addEvent(startDateTime, endTime, new LinkedList<Indicator>(), notes,
                        startLoc, endLoc);});
            } else if (startMap.containsKey(startName)) {
                LocationAPI.requestLocation(getActivity(), location -> {
                        WebResourceAPI.MapLocation startLoc = startMap.get(startName);
                        WebResourceAPI.MapLocation endLoc = new WebResourceAPI.MapLocation(location);
                        WebResourceAPI.getWeatherForecast(getContext(), startLoc.lat, startLoc.lon, epoch, res1 -> {
                            WebResourceAPI.getWeatherForecast(getContext(), endLoc.lat, endLoc.lon, epoch, res2 -> {
                                boolean[] startResults = IndicatorResults.indicatorResults(res1);
                                boolean[] endResults = IndicatorResults.indicatorResults(res2);
                                for (int i = 0; i < 4; i++) {
                                    startResults[i] |= endResults[i];
                                }
                                viewModel.processWeatherIndicators(startResults);
                                eventHandler.addEvent(startDateTime, endTime, IndicatorHelper.fromArray(startResults), notes,
                                        startLoc, endLoc);
                                }, e -> {
                                boolean[] startResults = IndicatorResults.indicatorResults(res1);
                                viewModel.processWeatherIndicators(startResults);
                                eventHandler.addEvent(startDateTime, endTime, IndicatorHelper.fromArray(startResults), notes,
                                        startLoc, endLoc);
                            });
                            },e -> eventHandler.addEvent(startDateTime, endTime, new LinkedList<Indicator>(), notes,
                                    startMap.get(startName), WebResourceAPI.MapLocation.getDefaultMapLocation()));
                            }, e -> eventHandler.addEvent(startDateTime, endTime, new LinkedList<Indicator>(), notes,
                        startMap.get(startName), WebResourceAPI.MapLocation.getDefaultMapLocation()));

            } else if (endMap.containsKey(endName)) {
                LocationAPI.requestLocation(getActivity(), location -> {
                    WebResourceAPI.MapLocation startLoc = new WebResourceAPI.MapLocation(location);
                    WebResourceAPI.MapLocation endLoc = endMap.get(endName);
                    WebResourceAPI.getWeatherForecast(getContext(), startLoc.lat, startLoc.lon, epoch, res1 -> {
                        WebResourceAPI.getWeatherForecast(getContext(), endLoc.lat, endLoc.lon, epoch, res2 -> {
                            boolean[] startResults = IndicatorResults.indicatorResults(res1);
                            boolean[] endResults = IndicatorResults.indicatorResults(res2);
                            for (int i = 0; i < 4; i++) {
                                startResults[i] |= endResults[i];
                            }
                            viewModel.processWeatherIndicators(startResults);
                            eventHandler.addEvent(startDateTime, endTime, IndicatorHelper.fromArray(startResults), notes,
                                    startLoc, endLoc);
                        }, e -> {
                            boolean[] startResults = IndicatorResults.indicatorResults(res1);
                            viewModel.processWeatherIndicators(startResults);
                            eventHandler.addEvent(startDateTime, endTime, IndicatorHelper.fromArray(startResults), notes,
                                    startLoc, endLoc);
                        });
                    },e -> eventHandler.addEvent(startDateTime, endTime, new LinkedList<Indicator>(), notes,
                            startMap.get(startName), WebResourceAPI.MapLocation.getDefaultMapLocation()));
                }, e -> eventHandler.addEvent(startDateTime, endTime, new LinkedList<Indicator>(), notes,
                        startMap.get(startName), WebResourceAPI.MapLocation.getDefaultMapLocation()));
            } else {
                eventHandler.addEvent(startDateTime, endTime, new LinkedList<Indicator>(), notes,
                        WebResourceAPI.MapLocation.getDefaultMapLocation(), WebResourceAPI.MapLocation.getDefaultMapLocation());
            }
        });

    }

    // shows time pickers
    private void startPopTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                routeModel.setStartHour(hourOfDay);
                routeModel.setStartMin(minute);
                startTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, routeModel.getStartHour(), routeModel.getStartMin(), true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void endPopTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                routeModel.setEndHour(hourOfDay);
                routeModel.setEndMin(minute);
                endTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, routeModel.getStartHour(), routeModel.getStartMin(), true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
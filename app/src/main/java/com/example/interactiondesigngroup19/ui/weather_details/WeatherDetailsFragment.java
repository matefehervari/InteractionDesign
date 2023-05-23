package com.example.interactiondesigngroup19.ui.weather_details;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActionBar;
import android.os.Bundle;
import org.json.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.interactiondesigngroup19.R;
import com.example.interactiondesigngroup19.apis.LocationAPI;
import com.example.interactiondesigngroup19.apis.WebResourceAPI;
import com.example.interactiondesigngroup19.databinding.FragmentCalendarBinding;
import com.example.interactiondesigngroup19.databinding.FragmentWeatherDetailsBinding;
import com.example.interactiondesigngroup19.ui.calendar.CalendarViewModel;
import com.example.interactiondesigngroup19.ui.home.HomeViewModel;
import com.google.android.material.tabs.TabLayout;

public class WeatherDetailsFragment extends Fragment {
    private FragmentWeatherDetailsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        CalendarViewModel calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout linearLayout = binding.WeatherDetailsLinearLayout;

        linearLayout.removeAllViews();

        for (int i = 0; i < 20; i ++) {
            TableRow.LayoutParams param1 = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    3
            );

            TableRow.LayoutParams param2 = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    4
            );

            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(
                    new TableRow.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, 150
                    )
            );
            tableRow.setWeightSum(10);

            TextView textView1 = new TextView(getContext());
            textView1.setText("Loading");
            textView1.setLayoutParams(param1);
            textView1.setTextSize(30);
            textView1.setGravity(Gravity.CENTER);;
            tableRow.addView(textView1);

            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.close));
            imageView.setLayoutParams(param2);
            tableRow.addView(imageView);

            TextView textView2 = new TextView(getContext());
            textView2.setText("Loading");
            textView2.setLayoutParams(param1);
            textView2.setTextSize(30);
            textView2.setGravity(Gravity.CENTER);
            tableRow.addView(textView2);

            linearLayout.addView(tableRow);
        }

        LocationAPI.ensureLocationClientAndRequester(getActivity());
        LocationAPI.requestLocation(getActivity(), location -> {

            WebResourceAPI.listWeatherForecast(getContext(), location, forecasts -> {
                linearLayout.removeAllViews();
                for (int i = 0; i < forecasts.size(); i++) {
                    TableRow.LayoutParams param1 = new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.MATCH_PARENT,
                            3
                    );

                    TableRow.LayoutParams param2 = new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.MATCH_PARENT,
                            4
                    );

                    TableRow tableRow = new TableRow(getContext());
                    tableRow.setLayoutParams(
                            new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, 150
                            )
                    );
                    tableRow.setWeightSum(10);

                    TextView textView1 = new TextView(getContext());
                    textView1.setText(forecasts.get(i).timeText);
                    textView1.setLayoutParams(param1);
                    textView1.setTextSize(20);
                    textView1.setGravity(Gravity.CENTER);;
                    tableRow.addView(textView1);

                    String generalWeatherDescription = forecasts.get(i).main.toLowerCase();

                    float currentTemperature = forecasts.get(i).temp;

                    // Formats the weather information for calculating front page information
                    // Deals with the temperature and the main image
                    String formattedTemp = (int) (currentTemperature - 273.15) + "°";

                    int imageId = 0;
                    switch (generalWeatherDescription) {
                        case "sunny":
                            imageId = R.drawable.sunny_main_image;
                            break;
                        case "snow":
                            imageId = R.drawable.snowy_main_image;
                            break;
                        case "rain":
                        case "drizzle":
                            imageId = R.drawable.rainy_main_image;
                            break;
                        case "clouds":
                        default:
                            imageId = R.drawable.cloudy_main_image;
                            break;
                    }
                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageDrawable(getResources().getDrawable(imageId));
                    imageView.setLayoutParams(param2);
                    tableRow.addView(imageView);

                    TextView textView2 = new TextView(getContext());
                    textView2.setText(formattedTemp);
                    textView2.setLayoutParams(param1);
                    textView2.setTextSize(30);
                    textView2.setGravity(Gravity.CENTER);
                    tableRow.addView(textView2);

                    linearLayout.addView(tableRow);

                }
            }, e-> {});
        }, e -> {});

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
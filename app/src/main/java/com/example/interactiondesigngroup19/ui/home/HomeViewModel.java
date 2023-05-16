package com.example.interactiondesigngroup19.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.interactiondesigngroup19.R;

import java.time.Instant;

public class HomeViewModel extends ViewModel {

    private static MutableLiveData<String> mText;
    private static int mImageID;

    public HomeViewModel() {
        mText = new MutableLiveData<>();


        // Get weather information in some format to store in these variables
        // EDIT: Edit hard-coded assignments to fetch from weather object API
        String generalWeatherDescription = "sunny";

        float probOfPrecipiation = 0.0f;
        float volumeOfPrecipitation = 0.0f;
        float windSpeed = 0.0f;

        float minTemperature = 298.0f;
        float maxTemperature = 300.0f;

        // N.B. Or manually assign if that can be done
        float currentTemperature = 0.5f * (minTemperature + maxTemperature);

        // N.B. Could alternatively convert to strings and compare like that
        Instant currentTime = Instant.now();
        Instant sunriseTime = Instant.now();
        Instant sunsetTime = Instant.now();

        // Formats the weather information for calculating front page information
        // Deals with the temperature and the main image
        String formattedTemp = Integer.toString((int) currentTemperature) + "°C";
        mText.setValue(formattedTemp);

        switch (generalWeatherDescription) {
            case "sunny":
                mImageID = R.drawable.sunny_main_image;
                break;
            case "cloudy":
                mImageID = R.drawable.cloudy_main_image;
                break;
            case "rainy":
                mImageID = R.drawable.rainy_main_image;
                break;
        }

        // Deals with boolean indicator icon values
        // N.B. Change some parameter for scaling and greyscale factor? (visual on/off)
        boolean rainIndicator = volumeOfPrecipitation > 0.5 && probOfPrecipiation > 0.15;
        boolean windIndicator = windSpeed > 4.5;
        boolean coatIndicator = rainIndicator || (currentTemperature < 298.0f && currentTemperature - 0.8f * windSpeed < 278.0f);
        boolean lightIndicator = currentTime.compareTo(sunriseTime) < 0 || currentTime.compareTo(sunsetTime) > 0;

    }

    public static LiveData<String> getText() {
        return mText;
    }

    public static int getImageID() {
        return mImageID;
    }
}
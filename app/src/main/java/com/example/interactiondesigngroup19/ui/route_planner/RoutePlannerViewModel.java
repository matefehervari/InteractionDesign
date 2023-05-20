package com.example.interactiondesigngroup19.ui.route_planner;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.interactiondesigngroup19.R;
import com.example.interactiondesigngroup19.apis.WebResourceAPI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.Instant;
import java.time.LocalDateTime;

public class RoutePlannerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private static int rainTint, windTint, coatTint, lightTint;
    private static float rainScale, windScale, coatScale, lightScale;

    private static long time;

    public RoutePlannerViewModel() {
        mText = new MutableLiveData<>();

        // Get context for the current request (N.B. but not used by getWeatherForecast() ?)
        Context currentContext = null;

        // Get location from somewhere
        Location currentLocation = new Location("");
        currentLocation.setLatitude(52.2053844);
        currentLocation.setLongitude(0.1189721);

        // Get 'time' as a long?
        time = 0;

        OnSuccessListener<WebResourceAPI.WeatherResult> weatherResultListener = new OnSuccessListener<WebResourceAPI.WeatherResult>() {
            @Override
            public void onSuccess(WebResourceAPI.WeatherResult weatherResult) {
                processWeatherRequest(weatherResult);
            }
        };

        // Deal with failure of API request?
        OnFailureListener failureListener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        };

        WebResourceAPI.getWeatherForecast(currentContext, currentLocation, time, weatherResultListener, failureListener);

        // processWeatherRequest();

    }

    public static void processWeatherRequest(WebResourceAPI.WeatherResult weatherResult) {

        float probOfPrecipiation = weatherResult.pop;
        float volumeOfPrecipitation = weatherResult.rain;
        float windSpeed = weatherResult.windSpeed;
        float currentTemperature = weatherResult.temp;

        // N.B. Could alternatively convert to strings and compare like that
        Instant currentTime = Instant.now();
        Instant sunriseTime = Instant.now();
        Instant sunsetTime = Instant.now();

        // Deals with boolean indicator icon values
        boolean rainIndicator = volumeOfPrecipitation > 0.5 && probOfPrecipiation > 0.15;
        boolean windIndicator = windSpeed > 4.5;
        boolean coatIndicator = rainIndicator || (currentTemperature < 298.0f && currentTemperature - 0.8f * windSpeed < 278.0f);
        boolean lightIndicator = currentTime.compareTo(sunriseTime) < 0 || currentTime.compareTo(sunsetTime) > 0;

        // Sets the tint and scaling for the indicator icon values
        rainTint = rainIndicator ? R.color.rain_indicator_on : R.color.indicator_off;
        windTint = windIndicator ? R.color.wind_indicator_on : R.color.indicator_off;
        coatTint = coatIndicator ? R.color.coat_indicator_on : R.color.indicator_off;
        lightTint = lightIndicator ? R.color.light_indicator_on : R.color.indicator_off;

        rainScale = rainIndicator ? 1.0f : 0.5f;
        windScale = windIndicator ? 1.0f : 0.5f;
        coatScale = coatIndicator ? 1.0f : 0.5f;
        lightScale = lightIndicator ? 1.0f : 0.5f;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public static int getRainTint() {
        return rainTint;
    }
    public static int getWindTint() {
        return windTint;
    }
    public static int getCoatTint() {
        return coatTint;
    }
    public static int getLightTint() {
        return lightTint;
    }

    public static float getRainScale() {
        return rainScale;
    }
    public static float getWindScale() {
        return windScale;
    }
    public static float getCoatScale() {
        return coatScale;
    }
    public static float getLightScale() {
        return lightScale;
    }

    public void setTime(long Time){time = Time;}

}
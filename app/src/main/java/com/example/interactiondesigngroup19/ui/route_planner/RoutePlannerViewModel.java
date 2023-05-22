package com.example.interactiondesigngroup19.ui.route_planner;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.*;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.interactiondesigngroup19.MainActivity;
import com.example.interactiondesigngroup19.R;
import com.example.interactiondesigngroup19.apis.WebResourceAPI;
import com.example.interactiondesigngroup19.ui.calendar.CalendarEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.Instant;

public class RoutePlannerViewModel extends ViewModelProvider.NewInstanceFactory {

    private MutableLiveData<String> mText;
    private int mImageID;

    private Location location;

    private Context currentContext;
    private long time;

    private OnSuccessListener<WebResourceAPI.WeatherResult> weatherResultListener;
    private OnFailureListener failureListener;
    private Application mApplication;
    private int rainTint, windTint, coatTint, lightTint;
    private float rainScale, windScale, coatScale, lightScale;
    private int startHour, startMin, endHour, endMin;

    public RoutePlannerViewModel(Application application) {
        mText = new MutableLiveData<>();

        // processWeatherRequest();
    }

    public void processWeatherRequest(WebResourceAPI.WeatherResult weatherResult) {

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

    public int getImageID() {
        return mImageID;
    }

    public int getRainTint() {
        return rainTint;
    }
    public int getWindTint() {
        return windTint;
    }
    public int getCoatTint() {
        return coatTint;
    }
    public int getLightTint() {
        return lightTint;
    }

    public float getRainScale() {
        return rainScale;
    }
    public float getWindScale() {
        return windScale;
    }
    public float getCoatScale() {
        return coatScale;
    }
    public float getLightScale() {
        return lightScale;
    }

    public int getStartHour() { return startHour; }

    public void setStartHour(int startHour) { this.startHour = startHour; }

    public int getStartMin() { return startMin; }

    public void setStartMin(int startMin) { this.startMin = startMin; }

    public int getEndHour() {return endHour; }

    public void setEndHour(int endHour) {this.endHour = endHour; }

    public int getEndMin() { return endMin; }
    public void setEndMin(int endMin) { this.endMin = endMin; }

    public void callAPI(Application application) {
        mApplication = application;
        // Get context for the current request (N.B. but not used by getWeatherForecast() ?)
        Context currentContext = mApplication.getApplicationContext();

        // Get location from somewhere
        Location location = new Location("");
        location.setLatitude(52.21109);
        location.setLongitude(0.09135);
        ;

        // Get 'time' as a long?
        long time = System.currentTimeMillis();

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
                rainTint = R.color.indicator_off;
                windTint = R.color.indicator_off;
                coatTint = R.color.indicator_off;
                lightTint = R.color.indicator_off;
            }
        };

        WebResourceAPI.getWeatherForecast(currentContext, location, time, weatherResultListener, failureListener);
    }
}
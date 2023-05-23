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
import com.example.interactiondesigngroup19.apis.IndicatorResults;
import com.example.interactiondesigngroup19.apis.WebResourceAPI;
import com.example.interactiondesigngroup19.ui.calendar.CalendarEvent;
import com.example.interactiondesigngroup19.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.Instant;

public class RoutePlannerViewModel extends ViewModelProvider.NewInstanceFactory {

    private MutableLiveData<String> mText;

    private MutableLiveData<HomeViewModel.WeatherUIData> weatherUIData;

    private Context currentContext;
    private Application mApplication;
    private int rainTint, windTint, coatTint, lightTint;
    private float rainScale, windScale, coatScale, lightScale;
    private int startHour, startMin, endHour, endMin;

    public RoutePlannerViewModel(Application application) {
        mText = new MutableLiveData<>();
        weatherUIData = new MutableLiveData<>(new HomeViewModel.WeatherUIData(0,
                R.color.indicator_off, R.color.indicator_off, R.color.indicator_off, R.color.indicator_off,
                0.5f, 0.5f, 0.5f, 0.5f));
        // processWeatherRequest();
    }

    public void processWeatherIndicators(boolean[] indicatorResults) {
        HomeViewModel.MutableWeatherUIData tempW = new HomeViewModel.MutableWeatherUIData(weatherUIData.getValue());

        // Sets the tint and scaling for the indicator icon values
        tempW.rainTint = indicatorResults[0] ? R.color.rain_indicator_on : R.color.indicator_off;
        tempW.windTint = indicatorResults[1] ? R.color.wind_indicator_on : R.color.indicator_off;
        tempW.coatTint = indicatorResults[2] ? R.color.coat_indicator_on : R.color.indicator_off;
        tempW.lightTint = indicatorResults[3] ? R.color.light_indicator_on : R.color.indicator_off;

        tempW.rainScale = indicatorResults[0] ? 1.0f : 0.5f;
        tempW.windScale = indicatorResults[1] ? 1.0f : 0.5f;
        tempW.coatScale = indicatorResults[2] ? 1.0f : 0.5f;
        tempW.lightScale = indicatorResults[3] ? 1.0f : 0.5f;

        weatherUIData.setValue(new HomeViewModel.WeatherUIData(tempW));
    }

    public void processWeatherRequest(WebResourceAPI.WeatherResult weatherResult) {
        String generalWeatherDescription = weatherResult.main.toLowerCase();
        HomeViewModel.MutableWeatherUIData tempW = new HomeViewModel.MutableWeatherUIData(weatherUIData.getValue());

        // Deals with boolean indicator icon values
        boolean[] indicatorResults = IndicatorResults.indicatorResults(weatherResult);

        // Sets the tint and scaling for the indicator icon values
        tempW.rainTint = indicatorResults[0] ? R.color.rain_indicator_on : R.color.indicator_off;
        tempW.windTint = indicatorResults[1] ? R.color.wind_indicator_on : R.color.indicator_off;
        tempW.coatTint = indicatorResults[2] ? R.color.coat_indicator_on : R.color.indicator_off;
        tempW.lightTint = indicatorResults[3] ? R.color.light_indicator_on : R.color.indicator_off;

        tempW.rainScale = indicatorResults[0] ? 1.0f : 0.5f;
        tempW.windScale = indicatorResults[1] ? 1.0f : 0.5f;
        tempW.coatScale = indicatorResults[2] ? 1.0f : 0.5f;
        tempW.lightScale = indicatorResults[3] ? 1.0f : 0.5f;

        weatherUIData.setValue(new HomeViewModel.WeatherUIData(tempW));
    }


    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<HomeViewModel.WeatherUIData> getWeatherUI() { return weatherUIData; }

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
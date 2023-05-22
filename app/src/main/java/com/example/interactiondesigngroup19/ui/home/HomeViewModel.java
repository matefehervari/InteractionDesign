package com.example.interactiondesigngroup19.ui.home;

import android.app.Application;
import android.content.Context;
import android.location.Location;

import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.interactiondesigngroup19.R;
import com.example.interactiondesigngroup19.apis.LocationAPI;
import com.example.interactiondesigngroup19.apis.WebResourceAPI;
import com.google.android.material.snackbar.Snackbar;

import java.time.Instant;

public class HomeViewModel extends ViewModelProvider.NewInstanceFactory {

    private MutableLiveData<String> mText;

    public class WeatherUIData {
        public final int mImageID;
        public final int rainTint, windTint, coatTint, lightTint;
        public final float rainScale, windScale, coatScale, lightScale;

        public WeatherUIData(int mImageID, int rainTint, int windTint, int coatTint, int lightTint, float rainScale, float windScale, float coatScale, float lightScale) {
            this.mImageID = mImageID;
            this.rainTint = rainTint;
            this.windTint = windTint;
            this.coatTint = coatTint;
            this.lightTint = lightTint;
            this.rainScale = rainScale;
            this.windScale = windScale;
            this.coatScale = coatScale;
            this.lightScale = lightScale;
        }

        public WeatherUIData(MutableWeatherUIData w) {
            this.mImageID = w.mImageID;
            this.rainTint = w.rainTint;
            this.windTint = w.windTint;
            this.coatTint = w.coatTint;
            this.lightTint = w.lightTint;
            this.rainScale = w.rainScale;
            this.windScale = w.windScale;
            this.coatScale = w.coatScale;
            this.lightScale = w.lightScale;
        }
    }

    public class MutableWeatherUIData {
        public int mImageID;
        public int rainTint, windTint, coatTint, lightTint;
        public float rainScale, windScale, coatScale, lightScale;

        public MutableWeatherUIData(int mImageID, int rainTint, int windTint, int coatTint, int lightTint, float rainScale, float windScale, float coatScale, float lightScale) {
            this.mImageID = mImageID;
            this.rainTint = rainTint;
            this.windTint = windTint;
            this.coatTint = coatTint;
            this.lightTint = lightTint;
            this.rainScale = rainScale;
            this.windScale = windScale;
            this.coatScale = coatScale;
            this.lightScale = lightScale;
        }

        public MutableWeatherUIData(WeatherUIData w) {
            this.mImageID = w.mImageID;
            this.rainTint = w.rainTint;
            this.windTint = w.windTint;
            this.coatTint = w.coatTint;
            this.lightTint = w.lightTint;
            this.rainScale = w.rainScale;
            this.windScale = w.windScale;
            this.coatScale = w.coatScale;
            this.lightScale = w.lightScale;
        }
    }

    private MutableLiveData<WeatherUIData> weatherUIData;
    private Application mApplication;

    public HomeViewModel(Application application) {
        mText = new MutableLiveData<>();
        weatherUIData = new MutableLiveData<>(new WeatherUIData(0,
                R.color.indicator_off, R.color.indicator_off, R.color.indicator_off, R.color.indicator_off,
                1.0f, 1.0f, 1.0f, 1.0f));
        mApplication = application;

        // processWeatherRequest();
    }

    public void processWeatherRequest(WebResourceAPI.WeatherResult weatherResult) {

        String generalWeatherDescription = weatherResult.main.toLowerCase();
        MutableWeatherUIData tempW = new MutableWeatherUIData(weatherUIData.getValue());

        float probOfPrecipiation = weatherResult.pop;
        float volumeOfPrecipitation = weatherResult.rain;
        float windSpeed = weatherResult.windSpeed;
        float currentTemperature = weatherResult.temp;

        // N.B. Could alternatively convert to strings and compare like that
        Instant currentTime = Instant.now();
        Instant sunriseTime = Instant.now();
        Instant sunsetTime = Instant.now();

        // Formats the weather information for calculating front page information
        // Deals with the temperature and the main image
        String formattedTemp = (int) (currentTemperature - 273.15) + "°";
        mText.setValue(formattedTemp);

        switch (generalWeatherDescription) {
            case "sunny":
                tempW.mImageID = R.drawable.sunny_main_image;
                break;
            case "snow":
                tempW.mImageID = R.drawable.snowy_main_image;
                break;
            case "rain":
            case "drizzle":
                tempW.mImageID = R.drawable.rainy_main_image;
                break;
            case "clouds":
            default:
                tempW.mImageID = R.drawable.cloudy_main_image;
                break;
        }

        // Deals with boolean indicator icon values
        boolean rainIndicator = volumeOfPrecipitation > 0.5 && probOfPrecipiation > 0.15;
        boolean windIndicator = windSpeed > 4.5;
        boolean coatIndicator = rainIndicator || (currentTemperature < 298.0f && currentTemperature - 0.8f * windSpeed < 278.0f);
        boolean lightIndicator = currentTime.compareTo(sunriseTime) < 0 || currentTime.compareTo(sunsetTime) > 0;

        // Sets the tint and scaling for the indicator icon values
        tempW.rainTint = rainIndicator ? R.color.rain_indicator_on : R.color.indicator_off;
        tempW.windTint = windIndicator ? R.color.wind_indicator_on : R.color.indicator_off;
        tempW.coatTint = coatIndicator ? R.color.coat_indicator_on : R.color.indicator_off;
        tempW.lightTint = lightIndicator ? R.color.light_indicator_on : R.color.indicator_off;

        tempW.rainScale = rainIndicator ? 1.0f : 0.5f;
        tempW.windScale = windIndicator ? 1.0f : 0.5f;
        tempW.coatScale = coatIndicator ? 1.0f : 0.5f;
        tempW.lightScale = lightIndicator ? 1.0f : 0.5f;

        weatherUIData.setValue(new WeatherUIData(tempW));
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<WeatherUIData> getWeatherUI() { return weatherUIData; }

    public int getImageID() {
        return weatherUIData.getValue().mImageID;
    }

    public int getRainTint() {
        return weatherUIData.getValue().rainTint;
    }
    public int getWindTint() {
        return weatherUIData.getValue().windTint;
    }
    public int getCoatTint() { return weatherUIData.getValue().coatTint; }
    public int getLightTint() {
        return weatherUIData.getValue().lightTint;
    }

    public float getRainScale() {
        return weatherUIData.getValue().rainScale;
    }
    public float getWindScale() {
        return weatherUIData.getValue().windScale;
    }
    public float getCoatScale() {
        return weatherUIData.getValue().coatScale;
    }
    public float getLightScale() {
        return weatherUIData.getValue().lightScale;
    }

    public void callAPI(FragmentActivity activity, View view) {
        // Get context for the current request (N.B. but not used by getWeatherForecast() ?)
        Context currentContext = mApplication.getApplicationContext();

        // Get location from somewhere
        LocationAPI.requestLocation(activity, (Location loc) -> {
            WebResourceAPI.getWeatherCurrent(currentContext, loc, this::processWeatherRequest, (Exception e) -> {
                Snackbar.make(view, e.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                        .setAnchorView(view)
                        .setAction("Action", null).show();
                //Debug please remove
                processWeatherRequest(new WebResourceAPI.WeatherResult(1.3f, 265, 4.3f, 0.4f,
                        180.3f, 182.2f, 183.3f, 175.7f, System.currentTimeMillis(), "", 10000,
                        "sunny", "rain", 72, 0.3f, 0f, true));
            });
        }, (Exception e) -> {
            Snackbar.make(view, e.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                    .setAnchorView(view)
                    .setAction("Action", null).show();
            //Debug please remove
            processWeatherRequest(new WebResourceAPI.WeatherResult(1.3f, 265, 4.3f, 0.4f,
                    182.3f, 190.2f, 190.3f, 180.7f, System.currentTimeMillis(), "", 10000,
                    "rain", "rain", 72, 0.3f, 0f, true));
        });
    }

    public void callAPI(FragmentActivity activity) {
        // Get context for the current request (N.B. but not used by getWeatherForecast() ?)
        Context currentContext = mApplication.getApplicationContext();
        mText.setValue("Loading");

        // Get location from somewhere
        LocationAPI.requestLocation(activity, (Location loc) -> {
            WebResourceAPI.getWeatherCurrent(currentContext, loc, this::processWeatherRequest, (Exception e) -> {
                //Debug please remove
                processWeatherRequest(new WebResourceAPI.WeatherResult(1.3f, 265, 4.3f, 0.4f,
                        180.3f, 182.2f, 183.3f, 175.7f, System.currentTimeMillis(), "", 10000,
                        "sunny", "rain", 72, 0.3f, 0f, true));
            });
        }, (Exception e) -> {
            //Debug please remove
            processWeatherRequest(new WebResourceAPI.WeatherResult(1.3f, 265, 4.3f, 0.4f,
                    182.3f, 190.2f, 190.3f, 180.7f, System.currentTimeMillis(), "", 10000,
                    "rain", "rain", 72, 0.3f, 0f, true));
        });
    }
}
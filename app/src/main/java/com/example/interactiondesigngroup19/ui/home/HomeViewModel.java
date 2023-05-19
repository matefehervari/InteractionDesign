package com.example.interactiondesigngroup19.ui.home;

import static com.example.interactiondesigngroup19.apis.WebResourceAPI.CURRENT_WEATHER;

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

import org.json.JSONException;

import java.time.Instant;

public class HomeViewModel extends ViewModel {

    private static MutableLiveData<String> mText;
    private static int mImageID;

    private static int rainTint, windTint, coatTint, lightTint;
    private static float rainScale, windScale, coatScale, lightScale;

    public HomeViewModel() {
        mText = new MutableLiveData<>();

        // TESTING - to show that indicator icons work (this does NOT use the API call)
        mImageID = R.drawable.rainy_main_image;
        mText.setValue("30°");

        boolean rainIndicator = true;
        boolean windIndicator = false;
        boolean coatIndicator = true;
        boolean lightIndicator = false;

        rainTint = rainIndicator ? R.color.rain_indicator_on : R.color.indicator_off;
        windTint = windIndicator ? R.color.wind_indicator_on : R.color.indicator_off;
        coatTint = coatIndicator ? R.color.coat_indicator_on : R.color.indicator_off;
        lightTint = lightIndicator ? R.color.light_indicator_on : R.color.indicator_off;

        rainScale = rainIndicator ? 1.0f : 0.5f;
        windScale = windIndicator ? 1.0f : 0.5f;
        coatScale = coatIndicator ? 1.0f : 0.5f;
        lightScale = lightIndicator ? 1.0f : 0.5f;

    }

    public static void processWeatherRequest(WebResourceAPI.WeatherResult weatherResult) {

        String generalWeatherDescription = weatherResult.main.toLowerCase();

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
        String formattedTemp = ((int) currentTemperature) + "°";
        mText.setValue(formattedTemp);

        switch (generalWeatherDescription) {
            case "sunny":
                mImageID = R.drawable.sunny_main_image;
                break;
            case "snow":
                mImageID = R.drawable.snowy_main_image;
                break;
            case "rain":
            case "drizzle":
                mImageID = R.drawable.rainy_main_image;
                break;
            case "clouds":
            default:
                mImageID = R.drawable.cloudy_main_image;
                break;
        }

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

    public static LiveData<String> getText() {
        return mText;
    }

    public static int getImageID() {
        return mImageID;
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
}
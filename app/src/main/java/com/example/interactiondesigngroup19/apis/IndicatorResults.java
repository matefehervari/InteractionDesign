package com.example.interactiondesigngroup19.apis;

import com.example.interactiondesigngroup19.R;

import java.time.Instant;

public class IndicatorResults {

    public static boolean[] indicatorResults(WebResourceAPI.WeatherResult weatherResult) {
        float probOfPrecipitation = weatherResult.pop;
        float volumeOfPrecipitation = weatherResult.rain;
        float windSpeed = weatherResult.windSpeed;
        float currentTemperature = weatherResult.temp;
        // Deals with boolean indicator icon values
        boolean rainIndicator = volumeOfPrecipitation > 0.5 && probOfPrecipitation > 0.15;
        boolean windIndicator = windSpeed > 4.5;
        boolean coatIndicator = rainIndicator || (currentTemperature < 298.0f && currentTemperature - 0.8f * windSpeed < 278.0f);
        boolean lightIndicator = !weatherResult.day;

        return new boolean[] {rainIndicator, windIndicator, coatIndicator, lightIndicator};
    }
}

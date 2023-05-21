package com.example.interactiondesigngroup19.ui.util;

import androidx.annotation.NonNull;

import com.example.interactiondesigngroup19.R;
import com.example.interactiondesigngroup19.R.color;
import com.example.interactiondesigngroup19.R.drawable;

import java.util.LinkedList;
import java.util.List;

public class IndicatorHelper {
    public final int RAIN_INDEX = 0;
    public final int WIND_INDEX = 1;
    public final int COAT_INDEX = 2;
    public final int LIGHT_INDEX = 3;

    private static final int[] iconHandles = {drawable.rain_indicator_icon, drawable.wind_indicator_icon, drawable.coat_indicator_icon, drawable.light_indicator_icon};
    private static final int[] activeColorHandles = {color.rain_indicator_on, color.wind_indicator_on, color.coat_indicator_on, color.light_indicator_on};
    private static final int inactiveColorHandle = color.indicator_off;

    @NonNull
    public static List<Indicator> fromArray(boolean[] flags) throws IllegalArgumentException {
        if (flags.length != 4) {
            throw new IllegalArgumentException(String.format("Expected 4 boolean flags, got %d", flags.length));
        }
        LinkedList<Indicator> indicators = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            indicators.add(new Indicator(iconHandles[i], flags[i] ? activeColorHandles[i] : inactiveColorHandle));
        }

        return indicators;
    }
}

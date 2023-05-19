package com.example.interactiondesigngroup19.ui.util;

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
    private static final int[] activeColorHandles = {color.white, color.white, color.white, color.white};
    private static final int[] inactiveColorHandles = {color.white, color.white, color.white, color.white};

    static List<Indicator> fromArray(boolean[] flags) {
        LinkedList<Indicator> indicators = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            indicators.add(new Indicator(iconHandles[i], flags[i] ? activeColorHandles[i] : inactiveColorHandles[i]));
        }

        return indicators;
    }
}

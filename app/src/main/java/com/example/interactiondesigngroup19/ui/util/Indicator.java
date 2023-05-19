package com.example.interactiondesigngroup19.ui.util;

public class Indicator {


    private final int iconHandle;
    private final int colorHandle;

    public Indicator(int iconHandle, int colorHandle) {
        this.iconHandle = iconHandle;
        this.colorHandle = colorHandle;
    }

    public int getIconHandle() {
        return iconHandle;
    }

    public int getColorHandle() {
        return colorHandle;
    }
}

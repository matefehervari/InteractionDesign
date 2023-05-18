package com.example.interactiondesigngroup19.ui.weather_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WeatherDetailsViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public WeatherDetailsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the Weather Details fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
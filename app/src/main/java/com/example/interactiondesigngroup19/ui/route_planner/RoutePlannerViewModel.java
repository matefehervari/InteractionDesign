package com.example.interactiondesigngroup19.ui.route_planner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RoutePlannerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RoutePlannerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the route planner fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
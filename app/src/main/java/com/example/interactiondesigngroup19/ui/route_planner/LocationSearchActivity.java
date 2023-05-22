package com.example.interactiondesigngroup19.ui.route_planner;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.interactiondesigngroup19.R;
import com.example.interactiondesigngroup19.apis.LocationAPI;
import com.example.interactiondesigngroup19.apis.WebResourceAPI;

import java.util.List;

public class LocationSearchActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        LocationAPI.ensureLocationClientAndRequester(this);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // perform query
            LocationAPI.requestLocation(this, (Location location) ->
                WebResourceAPI.listSearchLocation(getApplicationContext(), location, query, 5,
                        (List<WebResourceAPI.MapLocation> mapLocs) -> {},
                        (Exception e) -> {}),
                    (Exception e) -> {});
        }
    }
}

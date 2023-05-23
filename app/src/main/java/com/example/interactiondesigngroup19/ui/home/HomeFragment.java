package com.example.interactiondesigngroup19.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.interactiondesigngroup19.MainActivity;
import com.example.interactiondesigngroup19.R;
import com.example.interactiondesigngroup19.apis.LocationAPI;
import com.example.interactiondesigngroup19.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        onViewCreated(this.getView(), savedInstanceState);

        return root;

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final HomeViewModel viewModel = new HomeViewModel(getActivity().getApplication());

        final TextView textView = binding.temperatureTextview;
        final ImageButton refresh = binding.refreshButton;
        final ImageView mainImage = binding.weatherIconImageview;

        final ImageView rainIndicatorImage = binding.rainIndicator;
        final ImageView windIndicatorImage = binding.windIndicator;
        final ImageView coatIndicatorImage = binding.coatIndicator;
        final ImageView lightIndicatorImage = binding.lightIndicator;

        viewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        viewModel.getWeatherUI().observe(getViewLifecycleOwner(), weatherUIData -> {

            // Changes the attributes of the indicator images to indicate on/off
            mainImage.setImageResource(weatherUIData.mImageID);

            Context currContext = getActivity().getApplication().getApplicationContext();

            rainIndicatorImage.setColorFilter(ContextCompat.getColor(currContext, weatherUIData.rainTint));
            windIndicatorImage.setColorFilter(ContextCompat.getColor(currContext, weatherUIData.windTint));
            coatIndicatorImage.setColorFilter(ContextCompat.getColor(currContext, weatherUIData.coatTint));
            lightIndicatorImage.setColorFilter(ContextCompat.getColor(currContext, weatherUIData.lightTint));

            rainIndicatorImage.setScaleX(weatherUIData.rainScale);
            rainIndicatorImage.setScaleY(weatherUIData.rainScale);
            windIndicatorImage.setScaleX(weatherUIData.windScale);
            windIndicatorImage.setScaleY(weatherUIData.windScale);
            coatIndicatorImage.setScaleX(weatherUIData.coatScale);
            coatIndicatorImage.setScaleY(weatherUIData.coatScale);
            lightIndicatorImage.setScaleX(weatherUIData.lightScale);
            lightIndicatorImage.setScaleY(weatherUIData.lightScale);
        });

        refresh.setOnClickListener(view1 -> {
            // Changes the attributes of the indicator images to indicate on/off
            viewModel.callAPI(getActivity(), view1);
        });

        LocationAPI.ensureLocationClientAndRequester(getActivity());
        viewModel.callAPI(getActivity());

        final ImageButton details = binding.InfoButton;
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_weather_details);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
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

        final TextView textView = binding.temperatureTextview;
        final ImageButton refresh = binding.refreshButton;
        final ImageView mainImage = binding.weatherIconImageview;

        final ImageView rainIndicatorImage = binding.rainIndicator;
        final ImageView windIndicatorImage = binding.windIndicator;
        final ImageView coatIndicatorImage = binding.coatIndicator;
        final ImageView lightIndicatorImage = binding.lightIndicator;

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Changes the attributes of the indicator images to indicate on/off
                HomeViewModel model = new HomeViewModel(getActivity().getApplication());
                model.callAPI(getActivity().getApplication());

                model.getText().observe(getViewLifecycleOwner(), textView::setText);
                mainImage.setImageResource(model.getImageID());

                /*
                rainIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), model.getRainTint()));
                windIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), model.getWindTint()));
                coatIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), model.getCoatTint()));
                lightIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), model.getLightTint()));
                
                rainIndicatorImage.setScaleX(model.getRainScale());
                rainIndicatorImage.setScaleY(model.getRainScale());
                windIndicatorImage.setScaleX(model.getWindScale());
                windIndicatorImage.setScaleY(model.getWindScale());
                coatIndicatorImage.setScaleX(model.getCoatScale());
                coatIndicatorImage.setScaleY(model.getCoatScale());
                lightIndicatorImage.setScaleX(model.getLightScale());
                lightIndicatorImage.setScaleY(model.getLightScale());
                 */

            }
        });

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
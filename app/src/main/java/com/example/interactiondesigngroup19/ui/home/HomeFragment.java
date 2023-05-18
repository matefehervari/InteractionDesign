package com.example.interactiondesigngroup19.ui.home;

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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.interactiondesigngroup19.R;
import com.example.interactiondesigngroup19.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        onViewCreated(this.getView(), savedInstanceState);

        return root;

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final TextView textView = binding.temperatureTextview;
        final ImageButton refresh = binding.refreshButton;
        final ImageView mainImage = binding.weatherIconImageview;

        final ImageView rainIndicatorImage = binding.imageView12;
        final ImageView windIndicatorImage = binding.imageView19;
        final ImageView coatIndicatorImage = binding.imageView20;
        final ImageView lightIndicatorImage = binding.imageView21;

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
                mainImage.setImageResource(HomeViewModel.getImageID());

                // Changes the attributes of the indicator images to indicate on/off
                rainIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), HomeViewModel.getRainTint()));
                windIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), HomeViewModel.getWindTint()));
                coatIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), HomeViewModel.getCoatTint()));
                lightIndicatorImage.setColorFilter(ContextCompat.getColor(getContext(), HomeViewModel.getLightTint()));
                
                rainIndicatorImage.setScaleX(HomeViewModel.getRainScale());
                rainIndicatorImage.setScaleY(HomeViewModel.getRainScale());
                windIndicatorImage.setScaleX(HomeViewModel.getWindScale());
                windIndicatorImage.setScaleY(HomeViewModel.getWindScale());
                coatIndicatorImage.setScaleX(HomeViewModel.getCoatScale());
                coatIndicatorImage.setScaleY(HomeViewModel.getCoatScale());
                lightIndicatorImage.setScaleX(HomeViewModel.getLightScale());
                lightIndicatorImage.setScaleY(HomeViewModel.getLightScale());

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
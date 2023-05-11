package com.example.interactiondesigngroup19.ui.home;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.interactiondesigngroup19.R;
import com.example.interactiondesigngroup19.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    ImageButton help, info, refresh;

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
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
package com.example.foodietoronto.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.foodietoronto.R;

public class SavedPostsFragment extends Fragment {

    private SavedPostsViewModel savedPostsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        savedPostsViewModel =
                ViewModelProviders.of(this).get(SavedPostsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_savedposts, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        savedPostsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
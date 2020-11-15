package com.example.foodietoronto.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SavedPostsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SavedPostsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is saved posts fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
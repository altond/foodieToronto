package com.example.foodietoronto.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeedViewModel extends ViewModel {

    private MutableLiveData<String> posts;

    public FeedViewModel() {
        posts = new MutableLiveData<>();
        //mText.setValue("This is feed fragment");
    }

    public LiveData<String> getText() {
        return posts;
    }


}
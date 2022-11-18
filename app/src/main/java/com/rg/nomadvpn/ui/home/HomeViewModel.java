package com.rg.nomadvpn.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> duration;

    public HomeViewModel() {
        duration = new MutableLiveData<>();
    }

    public LiveData<String> getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration.setValue(duration);
    }
}
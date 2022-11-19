package com.rg.nomadvpn.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> duration;
    private MutableLiveData<String> status;

    public HomeViewModel() {
        duration = new MutableLiveData<>();
        status = new MutableLiveData<>();
    }

    public LiveData<String> getDuration() {
        return duration;
    }

    public LiveData<String> getStatus() {
        return status;
    }

    public void setDuration(String duration) {
        this.duration.setValue(duration);
    }

    public void setStatus(String status) {
        this.status.setValue(status);
    }
}
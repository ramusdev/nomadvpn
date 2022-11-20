package com.rg.nomadvpn.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> duration;
    private MutableLiveData<String> status;
    private MutableLiveData<String> receiveIn;
    private MutableLiveData<String> receiveOut;

    public HomeViewModel() {
        duration = new MutableLiveData<>();
        status = new MutableLiveData<>();
        receiveIn = new MutableLiveData<>();
        receiveOut = new MutableLiveData<>();
    }

    public LiveData<String> getDuration() {
        return duration;
    }

    public LiveData<String> getStatus() {
        return status;
    }

    public LiveData<String> getReceiveIn() {
        return receiveIn;
    }

    public LiveData<String> getReceiveOut() {
        return receiveOut;
    }

    public void setDuration(String duration) {
        this.duration.setValue(duration);
    }

    public void setStatus(String status) {
        this.status.setValue(status);
    }

    public void setReceiveIn(String receiveIn) {
        this.receiveIn.setValue(receiveIn);
    }

    public void setReceiveOut(String receiveOut) {
        this.receiveOut.setValue(receiveOut);
    }
}
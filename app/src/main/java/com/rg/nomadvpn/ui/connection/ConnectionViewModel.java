package com.rg.nomadvpn.ui.connection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConnectionViewModel extends ViewModel {

    private MutableLiveData<String> duration;
    private MutableLiveData<String> status;
    private MutableLiveData<String> receiveIn;
    private MutableLiveData<String> receiveOut;
    private MutableLiveData<String> speedIn;
    private MutableLiveData<String> speedOut;

    public ConnectionViewModel() {
        duration = new MutableLiveData<>();
        status = new MutableLiveData<>();
        receiveIn = new MutableLiveData<>();
        receiveOut = new MutableLiveData<>();
        speedIn = new MutableLiveData<>();
        speedOut = new MutableLiveData<>();
    }

    public LiveData<String> getSpeedIn() {
        return speedIn;
    }

    public LiveData<String> getSpeedOut() {
        return speedOut;
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

    public void setSpeedIn(String speedIn) {
        this.speedIn.setValue(speedIn);
    }

    public void setSpeedOut(String speedOut) {
        this.speedOut.setValue(speedOut);
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
package com.yunyizapp.ticketmastersearch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//    All the code partly take a reference from @ChatGPT.

public class FavoritesViewModel extends ViewModel {

    public MutableLiveData<Integer> number = new MutableLiveData<>(0);

    public void addNumber() {
        int currentNumber = number.getValue() != null ? number.getValue() : 0;
        number.setValue(currentNumber + 1);
    }

    public LiveData<Integer> getNumber() {
        return number;
    }

}
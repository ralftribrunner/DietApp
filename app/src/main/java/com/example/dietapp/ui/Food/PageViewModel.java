package com.example.dietapp.ui.Food;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();


    private LiveData<String> mList = Transformations.map(mIndex, new Function<Integer, String>()  {

        @Override
        public String apply(Integer input) {


            if (input==1) {

                return "meals";
            }
            else return "favorites";

        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mList;
    }
}
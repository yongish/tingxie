package com.zhiyong.tingxie;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class TestViewModel extends AndroidViewModel {
    private TestRepository mRepository;
    private LiveData<List<TestItem>> mAllTestItems;

    public TestViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TestRepository(application);
        mAllTestItems = mRepository.getAllTestItems();
    }

    LiveData<List<TestItem>> getAllTestItems() {
        return mAllTestItems;
    }

    public void insert(Test test) {
        mRepository.insert(test);
    }
}

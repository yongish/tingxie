package com.zhiyong.tingxie;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class TestViewModel extends AndroidViewModel {
    private TestRepository mRepository;
//    private LiveData<List<Test>> mAllTests;

    private LiveData<List<TestItem>> mAllTestItems;

    public TestViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TestRepository(application);
//        mAllTests = mRepository.getAllTests();
        mAllTestItems = mRepository.getAllTestItems();
    }

//    LiveData<List<Test>> getAllTests() {
//        return mAllTests;
//    }
    LiveData<List<TestItem>> getAllTests() {
        return mAllTestItems;
    }

    // todo: getAllTestItems().

    public void insert(Test test) {
        mRepository.insert(test);
    }
}

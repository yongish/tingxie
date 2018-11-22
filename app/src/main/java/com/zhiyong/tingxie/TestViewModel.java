package com.zhiyong.tingxie;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class TestViewModel extends AndroidViewModel {
    private TestRepository mRepository;
    private LiveData<List<Test>> mAllTests;

    public TestViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TestRepository(application);
        mAllTests = mRepository.getAllTests();
    }

    LiveData<List<Test>> getAllTests() {
        return mAllTests;
    }

    public void insert(Test test) {
        mRepository.insert(test);
    }
}

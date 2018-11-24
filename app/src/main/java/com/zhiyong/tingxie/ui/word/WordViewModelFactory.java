package com.zhiyong.tingxie.ui.word;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class WordViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private int mQuizId;

    public WordViewModelFactory(Application application, int quizId) {
        mApplication = application;
        mQuizId = quizId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WordViewModel(mApplication, mQuizId);
    }
}

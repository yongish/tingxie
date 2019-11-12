package com.zhiyong.tingxie.ui.term;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

public class WordViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private long mQuizId;

    public WordViewModelFactory(Application application, long quizId) {
        mApplication = application;
        mQuizId = quizId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TermViewModel(mApplication, mQuizId);
    }
}

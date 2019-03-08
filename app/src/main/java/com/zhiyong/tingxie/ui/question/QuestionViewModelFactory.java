package com.zhiyong.tingxie.ui.question;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class QuestionViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private long mQuizId;

    public QuestionViewModelFactory(Application application, long quizId) {
        mApplication = application;
        mQuizId = quizId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new QuestionViewModel(mApplication, mQuizId);
    }
}

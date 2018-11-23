package com.zhiyong.tingxie;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.zhiyong.tingxie.db.Quiz;

import java.util.List;

public class QuizViewModel extends AndroidViewModel {
    private QuizRepository mRepository;
    private LiveData<List<QuizItem>> mAllQuizItems;

    public QuizViewModel(@NonNull Application application) {
        super(application);
        mRepository = new QuizRepository(application);
        mAllQuizItems = mRepository.getAllTestItems();
    }

    LiveData<List<QuizItem>> getAllTestItems() {
        return mAllQuizItems;
    }

    public void insert(Quiz quiz) {
        mRepository.insert(quiz);
    }
}

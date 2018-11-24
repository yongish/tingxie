package com.zhiyong.tingxie.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.zhiyong.tingxie.QuizRepository;
import com.zhiyong.tingxie.db.Quiz;

import java.util.List;

public class QuizViewModel extends AndroidViewModel {
    private QuizRepository mRepository;
    private LiveData<List<QuizItem>> mAllQuizItems;

    public QuizViewModel(@NonNull Application application) {
        super(application);
        mRepository = new QuizRepository(application, 0);
        mAllQuizItems = mRepository.getAllQuizItems();
    }

    LiveData<List<QuizItem>> getAllQuizItems() {
        return mAllQuizItems;
    }

    public void insert(Quiz quiz) {
        mRepository.insert(quiz);
    }
}

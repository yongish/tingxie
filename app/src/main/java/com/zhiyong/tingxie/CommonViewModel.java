package com.zhiyong.tingxie;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import com.zhiyong.tingxie.db.QuizPinyin;

// todo: Try placing duplicate functions of QuizViewMode and WordViewModel.
public abstract class CommonViewModel extends AndroidViewModel {
    protected QuizRepository mRepository;

    public CommonViewModel(Application application, int quizId) {
        super(application);
        mRepository = new QuizRepository(application, quizId);
    }

    void addQuizPinyin(QuizPinyin quizPinyin) {
        mRepository.insertQuizPinyin(quizPinyin);
    }

    QuizRepository getRepository() {
        return mRepository;
    }
}

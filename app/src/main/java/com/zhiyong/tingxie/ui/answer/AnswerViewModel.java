package com.zhiyong.tingxie.ui.answer;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

import com.zhiyong.tingxie.QuizRepository;
import com.zhiyong.tingxie.db.Question;

public class AnswerViewModel extends AndroidViewModel {
    private QuizRepository mRepository;

    public AnswerViewModel(@NonNull Application application) {
        super(application);
        mRepository = new QuizRepository(application, -1);
    }

    void insertQuestion(Question question) {
        mRepository.insertQuestion(question);
    }
}

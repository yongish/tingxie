package com.zhiyong.tingxie.ui.question;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.zhiyong.tingxie.ui.main.QuizRepository;
import com.zhiyong.tingxie.ui.word.WordItem;

import java.util.List;

public class QuestionViewModel extends AndroidViewModel {
    private QuizRepository mRepository;
    private LiveData<List<WordItem>>  mQuestionItems;

    public QuestionViewModel(@NonNull Application application, long quizId) {
        super(application);
        mRepository = new QuizRepository(application, quizId);
        mQuestionItems = mRepository.getRemainingQuestionsOfQuiz();
    }

    LiveData<List<WordItem>> getRemainingQuestions() {
        return mQuestionItems;
    }
}

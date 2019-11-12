package com.zhiyong.tingxie.ui.question;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.zhiyong.tingxie.db.Term;
import com.zhiyong.tingxie.ui.main.QuizRepository;

import java.util.List;

public class QuestionViewModel extends AndroidViewModel {
    private QuizRepository mRepository;
    private LiveData<List<Term>>  mQuestionItems;

    public QuestionViewModel(@NonNull Application application, long quizId) {
        super(application);
        mRepository = new QuizRepository(application, quizId);
        mQuestionItems = mRepository.getRemainingQuestionsOfQuiz();
    }

    LiveData<List<Term>> getRemainingQuestions() {
        return mQuestionItems;
    }
}

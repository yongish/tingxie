package com.zhiyong.tingxie.ui.answer;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

import com.zhiyong.tingxie.QuizRepository;
import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.ui.main.QuizItem;

public class AnswerViewModel extends AndroidViewModel {
    private QuizRepository mRepository;

    public AnswerViewModel(@NonNull Application application) {
        super(application);
        mRepository = new QuizRepository(application, -1);
    }

    void insertQuestion(Question question) {
        mRepository.insertQuestion(question);
    }

    void updateQuiz(QuizItem quizItem) {
        mRepository.updateQuiz(new Quiz(
                quizItem.getId(),
                quizItem.getDate(),
                quizItem.getTitle(),
                quizItem.getTotalWords(),
                quizItem.getNotLearned(),
                quizItem.getRound()
        ));
    }
}

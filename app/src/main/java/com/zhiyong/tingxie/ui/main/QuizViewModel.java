package com.zhiyong.tingxie.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.QuizPinyin;

import java.util.List;

public class QuizViewModel extends AndroidViewModel {
    private QuizRepository mRepository;
    private LiveData<List<QuizItem>> mAllQuizItems;
    private LiveData<List<QuizPinyin>> mQuizPinyins;
    private LiveData<List<Question>> mQuestions;

    public QuizViewModel(@NonNull Application application) {
        super(application);
        mRepository = new QuizRepository(application, -1);
        mAllQuizItems = mRepository.getAllQuizItems();
        mQuizPinyins = mRepository.getAllQuizPinyins();
        mQuestions = mRepository.getAllQuestions();
    }

    LiveData<List<QuizItem>> getAllQuizItems() {
        return mAllQuizItems;
    }

    LiveData<List<QuizPinyin>> getAllQuizPinyins() {
        return mQuizPinyins;
    }

    LiveData<List<Question>> getAllQuestions() {
        return mQuestions;
    }

    void insertQuiz(Quiz quiz) {
        mRepository.insertQuiz(quiz);
    }

    void updateQuiz(Quiz quiz) {
        mRepository.updateQuiz(quiz);
    }

    void deleteQuiz(long quizId) {
        mRepository.deleteQuiz(quizId);
    }

    void insertQuestions(List<Question> questions) {
        for (Question question : questions) {
            mRepository.insertQuestion(question);
        }
    }

    void insertQuizPinyins(List<QuizPinyin> quizPinyins) {
        for (QuizPinyin quizPinyin : quizPinyins) {
            mRepository.insertQuizPinyin(quizPinyin);
        }
    }
}

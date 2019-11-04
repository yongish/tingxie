package com.zhiyong.tingxie.ui.word;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.zhiyong.tingxie.ui.main.QuizRepository;
import com.zhiyong.tingxie.db.QuizPinyin;

import java.util.List;

class WordViewModel extends AndroidViewModel {
    private LiveData<List<WordItem>> mWordItems;
    private QuizRepository mRepository;

    private TermRepository termRepository;
    private MutableLiveData<TermResponse> mutableLiveData;

    WordViewModel(Application application, long quizId) {
        super(application);
        mRepository = new QuizRepository(application, quizId);
        mWordItems = mRepository.getWordItemsOfQuiz();

        termRepository = TermRepository.getInstance(
                application.getSharedPreferences("login", Context.MODE_PRIVATE)
        );
        mutableLiveData = termRepository.getTerms();
    }

    LiveData<TermResponse> getTerms() {
        return mutableLiveData;
    }

    LiveData<List<WordItem>> getWordItemsOfQuiz() {
        return mWordItems;
    }

    void addWord(long quizId, String word, String pinyin) {
        mRepository.addWord(quizId, word, pinyin);
    }

    void deleteWord(QuizPinyin quizPinyin) {
        mRepository.deleteWord(quizPinyin);
    }

    void addQuizPinyin(QuizPinyin quizPinyin) {
        mRepository.insertQuizPinyin(quizPinyin);
    }

    void updateQuestions(long quizId) {
        mRepository.updateQuestions(quizId);
    }
}

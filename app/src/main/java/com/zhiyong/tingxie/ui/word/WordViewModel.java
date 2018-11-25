package com.zhiyong.tingxie.ui.word;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.zhiyong.tingxie.QuizRepository;
import com.zhiyong.tingxie.db.QuizPinyin;

import java.util.List;

class WordViewModel extends AndroidViewModel {
    private LiveData<List<WordItem>> mWordItems;
    private QuizRepository mRepository;

    WordViewModel(Application application, int quizId) {
        super(application);
        mRepository = new QuizRepository(application, quizId);
        mWordItems = mRepository.getWordItemsOfQuiz();
    }

    LiveData<List<WordItem>> getWordItemsOfQuiz() {
        return mWordItems;
    }

    void deleteWord(QuizPinyin quizPinyin) {
        mRepository.deleteWord(quizPinyin);
    }

    void addQuizPinyin(QuizPinyin quizPinyin) {
        mRepository.insertQuizPinyin(quizPinyin);
    }
}

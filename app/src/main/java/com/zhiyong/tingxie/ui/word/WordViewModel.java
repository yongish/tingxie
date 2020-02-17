package com.zhiyong.tingxie.ui.word;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.zhiyong.tingxie.QuizRepository;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.QuizPinyin;
import com.zhiyong.tingxie.ui.main.QuizItem;

import java.util.List;

class WordViewModel extends AndroidViewModel {
    private LiveData<List<WordItem>> mWordItems;
    private LiveData<QuizItem> mQuizItem;
    private QuizRepository mRepository;

    WordViewModel(Application application, long quizId) {
        super(application);
        mRepository = new QuizRepository(application, quizId);
        mWordItems = mRepository.getWordItemsOfQuiz();
        mQuizItem = mRepository.getQuizItem();
    }

    LiveData<List<WordItem>> getWordItemsOfQuiz() {
        return mWordItems;
    }

    LiveData<QuizItem> getQuizItem() {
        return mQuizItem;
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

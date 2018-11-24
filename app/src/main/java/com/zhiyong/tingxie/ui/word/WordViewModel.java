package com.zhiyong.tingxie.ui.word;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.zhiyong.tingxie.PinyinRoomDatabase;
import com.zhiyong.tingxie.QuizRepository;

import java.util.List;

public class WordViewModel extends AndroidViewModel {
    private LiveData<List<WordItem>> mWordItems;
    private QuizRepository mRepository;
    private PinyinRoomDatabase db;

    public WordViewModel(Application application, int quizId) {
        super(application);

        db = PinyinRoomDatabase.getDatabase(getApplication());

        mWordItems = db.pinyinDao().getWordItemsOfQuizId(quizId);
    }

    LiveData<List<WordItem>> getWordItemsOfQuiz() {
        return mWordItems;
    }
}

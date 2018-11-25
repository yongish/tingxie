package com.zhiyong.tingxie;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.QuizPinyin;
import com.zhiyong.tingxie.ui.main.QuizItem;
import com.zhiyong.tingxie.ui.word.WordItem;

import java.util.List;

/* A Repository is a class that abstracts access to multiple data sources.
A Repository manages query threads and allows you to use multiple backends.
In the most common example, the Repository implements the logic for deciding whether to fetch data
from a network or use results cached in the local database. */
public class QuizRepository {

    private static final String TAG = "QuizRepository";

    private QuizDao mQuizDao;
    private LiveData<List<QuizItem>> mAllQuizItems;
    private LiveData<List<WordItem>> mAllWordItems;

    public QuizRepository(Application application, int quizId) {
        PinyinRoomDatabase db = PinyinRoomDatabase.getDatabase(application);
        mQuizDao = db.pinyinDao();
        Log.d(TAG, "QuizRepository: ");

        mAllQuizItems = mQuizDao.getAllQuizItems();
        mAllWordItems = mQuizDao.getWordItemsOfQuizId(quizId);
    }

    public LiveData<List<QuizItem>> getAllQuizItems() {
        return mAllQuizItems;
    }

    public LiveData<List<WordItem>> getWordItemsOfQuiz() {
        return mAllWordItems;
    }

    public void insert (Quiz quiz) {
        new insertAsyncTask(mQuizDao).execute(quiz);
    }

    private static class insertAsyncTask extends AsyncTask<Quiz, Void, Void> {

        private QuizDao mAsyncTaskDao;

        insertAsyncTask(QuizDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Quiz... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    // Only delete QuizPinyin object.
    public void deleteWord(QuizPinyin quizPinyin) {
        new deleteWordAsyncTask(mQuizDao).execute(quizPinyin);
    }

    private static class deleteWordAsyncTask extends AsyncTask<QuizPinyin, Void, Void> {
        private QuizDao mAsyncTaskDao;

        deleteWordAsyncTask(QuizDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(QuizPinyin... params) {
            // Word and Pinyin are immutable. Only QuizPinyin object is deleted.
            mAsyncTaskDao.deleteQuizPinyin(params[0].getQuiz_id(), params[0].getPinyin_id());
            return null;
        }
    }

    // Undo a just deleted word.
    public void insertQuizPinyin(QuizPinyin quizPinyin) {
        new insertQuizPinyinAsyncTask(mQuizDao).execute(quizPinyin);
    }

    private static class insertQuizPinyinAsyncTask extends AsyncTask<QuizPinyin, Void, Void> {
        private QuizDao mAsyncTaskDao;

        insertQuizPinyinAsyncTask(QuizDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(QuizPinyin... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void insertWord(WordItem wordItem) {

    }
}

package com.zhiyong.tingxie.ui.main;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.zhiyong.tingxie.PinyinRoomDatabase;
import com.zhiyong.tingxie.QuizDao;
import com.zhiyong.tingxie.db.Quiz;

import java.util.List;

/* A Repository is a class that abstracts access to multiple data sources.
A Repository manages query threads and allows you to use multiple backends.
In the most common example, the Repository implements the logic for deciding whether to fetch data
from a network or use results cached in the local database. */
public class QuizRepository {

    private static final String TAG = "QuizRepository";

    private QuizDao mQuizDao;
    private LiveData<List<QuizItem>> mAllQuizItems;

    QuizRepository(Application application) {
        PinyinRoomDatabase db = PinyinRoomDatabase.getDatabase(application);
        mQuizDao = db.pinyinDao();
        Log.d(TAG, "QuizRepository: ");

        // todo: Construct mAllQuizItems here.
        mAllQuizItems = mQuizDao.getAllQuizItems();
    }

    LiveData<List<QuizItem>> getAllQuizItems() {
        return mAllQuizItems;
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
}

package com.example.android;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/* A Repository is a class that abstracts access to multiple data sources.
A Repository manages query threads and allows you to use multiple backends.
In the most common example, the Repository implements the logic for deciding whether to fetch data
from a network or use results cached in the local database. */
public class TestRepository {
    private PinyinDao mPinyinDao;
    private LiveData<List<Test>> mAllTests;

    TestRepository(Application application) {
        PinyinRoomDatabase db = PinyinRoomDatabase.getDatabase(application);
        mPinyinDao = db.pinyinDao();
        mAllTests = mPinyinDao.getAllTests();
    }

    LiveData<List<Test>> getAllTests() {
        return mAllTests;
    }

    public void insert (Test test) {
        new insertAsyncTask(mPinyinDao).execute(test);
    }

    private static class insertAsyncTask extends AsyncTask<Test, Void, Void> {

        private PinyinDao mAsyncTaskDao;

        insertAsyncTask(PinyinDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Test... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}

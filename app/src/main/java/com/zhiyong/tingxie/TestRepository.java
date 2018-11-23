package com.zhiyong.tingxie;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

/* A Repository is a class that abstracts access to multiple data sources.
A Repository manages query threads and allows you to use multiple backends.
In the most common example, the Repository implements the logic for deciding whether to fetch data
from a network or use results cached in the local database. */
public class TestRepository {

    private static final String TAG = "TestRepository";

    private TestDao mTestDao;
    private LiveData<List<TestItem>> mAllTestItems;

    TestRepository(Application application) {
        PinyinRoomDatabase db = PinyinRoomDatabase.getDatabase(application);
        mTestDao = db.pinyinDao();
        Log.d(TAG, "TestRepository: ");

        // todo: Construct mAllTestItems here.
        mAllTestItems = mTestDao.getAllTestItems();
    }

    LiveData<List<TestItem>> getAllTestItems() {
        return mAllTestItems;
    }

    public void insert (Test test) {
        new insertAsyncTask(mTestDao).execute(test);
    }

    private static class insertAsyncTask extends AsyncTask<Test, Void, Void> {

        private TestDao mAsyncTaskDao;

        insertAsyncTask(TestDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Test... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}

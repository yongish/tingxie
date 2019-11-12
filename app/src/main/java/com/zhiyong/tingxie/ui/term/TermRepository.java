package com.zhiyong.tingxie.ui.term;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.zhiyong.tingxie.PinyinRoomDatabase;
import com.zhiyong.tingxie.QuizDao;
import com.zhiyong.tingxie.db.Term;

import java.util.List;

public class TermRepository {

    private QuizDao mQuizDao;
    private static String uid;
    private LiveData<List<Term>> mTerms;

    public TermRepository(Application application, long quizId) {
        PinyinRoomDatabase db = PinyinRoomDatabase.getDatabase(application);
        mQuizDao = db.pinyinDao();

        SharedPreferences pref = application.getSharedPreferences("login", Context.MODE_PRIVATE);
        uid = pref.getString("uid", null);

        mTerms = mQuizDao.getTerms(uid, quizId);
    }

    public LiveData<List<Term>> getTerms() {
        return mTerms;
    }

    public void insert(Term term) {
        new insertTermAsyncTask(mQuizDao).execute(term);
    }

    private static class insertTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private QuizDao mAsyncTaskDao;

        insertTermAsyncTask(QuizDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            mAsyncTaskDao.insert(terms[0]);
            return null;
        }
    }

    public void delete(Term term) {
        new deleteTermAsyncTask(mQuizDao).execute(term);
    }

    private static class deleteTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private QuizDao mAsyncTaskDao;

        deleteTermAsyncTask(QuizDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            mAsyncTaskDao.deleteTerm(terms[0]);
            return null;
        }
    }

    // Placeholder code for backend.
//    private static FirebaseAuth auth;
//
//    public static TermRepository getInstance(SharedPreferences pref) {
//        if (termRepository == null) {
//            termRepository = new TermRepository();
//            auth = FirebaseAuth.getInstance();
//            uid = pref.getString("uid", null);
//        }
//        return termRepository;
//    }
//
//    private TermApi termApi;
//
//    public TermRepository() {
//        termApi = RetrofitService.createService(TermApi.class);
//    }
//
//    public MutableLiveData<TermResponse> getTerms() {
//        final MutableLiveData<TermResponse> terms = new MutableLiveData<>();
//        if (uid == null) {
//            throw new IllegalStateException("uid is null.");
//        }
//
//        termApi.getTermsList(uid).enqueue(new Callback<TermResponse>() {
//            @Override
//            public void onResponse(Call<TermResponse> call, Response<TermResponse> response) {
//                if (response.isSuccessful()) {
//                    terms.setValue(response.body());
//                } else if (response.code() == 401){
//                    // todo: Refresh token and try again.
//                } else {
//                    throw new IllegalStateException("Error in getting terms of uid: " + uid);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TermResponse> call, Throwable t) {
//                terms.setValue(null);
//            }
//        });
//        return terms;
//    }
}

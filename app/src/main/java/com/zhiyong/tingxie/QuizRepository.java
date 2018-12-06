package com.zhiyong.tingxie;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.zhiyong.tingxie.db.Pinyin;
import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.QuizPinyin;
import com.zhiyong.tingxie.db.Word;
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
    private LiveData<List<WordItem>> mWordItems;
    private LiveData<List<QuizPinyin>> mAllQuizPinyins;
    private LiveData<List<Question>> mAllQuestions;
    private LiveData<List<WordItem>> mPossibleQuestions;
    private LiveData<List<WordItem>> mRemainingQuestions;

    public QuizRepository(Application application, int quizId) {
        PinyinRoomDatabase db = PinyinRoomDatabase.getDatabase(application);
        mQuizDao = db.pinyinDao();
        Log.d(TAG, "QuizRepository: ");

        mAllQuizItems = mQuizDao.getAllQuizItems();
        mWordItems = mQuizDao.getWordItemsOfQuiz(quizId);
        mAllQuizPinyins = mQuizDao.getAllQuizPinyins();
        mAllQuestions = mQuizDao.getAllQuestions();
        mPossibleQuestions = mQuizDao.getPossibleQuestions(quizId);
        mRemainingQuestions = mQuizDao.getRemainingQuestions(quizId);
    }

    public LiveData<List<QuizItem>> getAllQuizItems() {
        return mAllQuizItems;
    }

    public LiveData<List<WordItem>> getWordItemsOfQuiz() {
        return mWordItems;
    }

    public LiveData<List<QuizPinyin>> getAllQuizPinyins() {
        return mAllQuizPinyins;
    }

    public LiveData<List<Question>> getAllQuestions() {
        return mAllQuestions;
    }

    public LiveData<List<WordItem>> getPossibleQuestionsOfQuiz() {
        return mPossibleQuestions;
    }

    public LiveData<List<WordItem>> getRemainingQuestionsOfQuiz() {
        return mRemainingQuestions;
    }

    public void insertQuiz(Quiz quiz) {
        new insertQuizAsyncTask(mQuizDao).execute(quiz);
    }

    private static class insertQuizAsyncTask extends AsyncTask<Quiz, Void, Void> {
        private QuizDao mAsyncTaskDao;

        insertQuizAsyncTask(QuizDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Quiz... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void insertQuestion(Question question) {
        new insertQuestionAsyncTask(mQuizDao).execute(question);
    }

    private static class insertQuestionAsyncTask extends AsyncTask<Question, Void, Void> {
        private QuizDao mAsyncTaskDao;

        insertQuestionAsyncTask(QuizDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Question... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void addWord(int quizId, String wordString, String pinyinString) {
        WordItem wordItem = new WordItem(quizId, wordString, pinyinString);
        new addWordAsyncTask(mQuizDao).execute(wordItem);
    }

    private static class addWordAsyncTask extends AsyncTask<WordItem, Void, Void> {
        private QuizDao mAsyncTaskDao;

        addWordAsyncTask(QuizDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(WordItem... params) {
            String pinyinString = params[0].getPinyinString();
            mAsyncTaskDao.insert(new Pinyin(pinyinString));
            mAsyncTaskDao.insert(new Word(params[0].getWordString(), pinyinString));
            mAsyncTaskDao.insert(new QuizPinyin(params[0].getQuizId(), pinyinString));
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
            mAsyncTaskDao.deleteQuizPinyin(params[0].getQuiz_id(), params[0].getPinyin_string());
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

    public void deleteQuiz(int quizId) {
        new deleteQuizItemAsyncTask(mQuizDao).execute(quizId);
    }

    private static class deleteQuizItemAsyncTask extends AsyncTask<Integer, Void, Void> {
        private QuizDao mAsyncTaskDao;

        deleteQuizItemAsyncTask(QuizDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            mAsyncTaskDao.deleteQuiz(params[0]);
            return null;
        }
    }
}

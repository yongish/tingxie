package com.zhiyong.tingxie.ui.term;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.Term;
import com.zhiyong.tingxie.ui.main.QuizItem;
import com.zhiyong.tingxie.ui.main.QuizRepository;

import java.util.List;

class TermViewModel extends AndroidViewModel {
    private LiveData<List<Term>> mTerms;
    private QuizRepository quizRepository;
    private TermRepository termRepository;
    private SharedPreferences prefs;
    private String uid;
//    private MutableLiveData<TermResponse> mutableLiveData;

    TermViewModel(Application application, long quizId) {
        super(application);
        quizRepository = new QuizRepository(application, quizId);
        termRepository = new TermRepository(application, quizId);
        mTerms = termRepository.getTerms();
        prefs = application.getSharedPreferences("login", Context.MODE_PRIVATE);
        uid = prefs.getString("uid", null);
        if (uid == null) {
            throw new IllegalStateException("UID is null");
        }


//        mutableLiveData = termRepository.getTerms();
    }

    LiveData<List<Term>> getTerms() {
        return mTerms;
    }

    void addWord(QuizItem quizItem, String word, String pinyin) {
        termRepository.insert(new Term(
                quizItem.getId(),
                uid,
                word,
                pinyin));

        // Asynchronous.
        Quiz quiz = new Quiz(
                quizItem.getId(), quizItem.getDate(), quizItem.getTitle(), uid,
                quizItem.getTotalTerms(), quizItem.getNotLearned(), quizItem.getRoundsCompleted()
        );
        quizRepository.updateQuiz(quiz);
    }

//    void deleteWord(QuizPinyin quizPinyin) {
//        quizRepository.deleteWord(quizPinyin);
//    }
    void deleteTerm(Term term) {
        termRepository.delete(term);
    }

    void addTerm(Term term) {
        termRepository.insert(term);
    }
}

//        termRepository = TermRepository.getInstance(
//                application.getSharedPreferences("login", Context.MODE_PRIVATE)
//                );
//    LiveData<TermResponse> getTerms() {
//        return mutableLiveData;
//    }

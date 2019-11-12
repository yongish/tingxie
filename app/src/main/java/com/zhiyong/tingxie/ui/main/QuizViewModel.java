package com.zhiyong.tingxie.ui.main;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.Term;
import com.zhiyong.tingxie.ui.term.TermRepository;

import java.util.List;

public class QuizViewModel extends AndroidViewModel {
    private QuizRepository mRepository;
    private TermRepository termRepository;

    private LiveData<List<QuizItem>> mAllQuizItems;
    private LiveData<List<Question>> mQuestions;

//    private MutableLiveData<QuizResponse> mutableLiveData;

    public QuizViewModel(@NonNull Application application) {
        super(application);
        mRepository = new QuizRepository(application, -1);
        termRepository = new TermRepository(application, -1);
//        mRepository = QuizRepository.getInstance(
//                application.getSharedPreferences("login", Context.MODE_PRIVATE)
//        );
//        mutableLiveData = mRepository.getQuizItems();

        mAllQuizItems = mRepository.getAllQuizItems();
        mQuestions = mRepository.getAllQuestions();
    }

    LiveData<List<QuizItem>> getAllQuizItems() {
        return mAllQuizItems;
    }

    LiveData<List<Question>> getAllQuestions() {
        return mQuestions;
    }

    void insertQuiz(Quiz quiz) {
        mRepository.insertQuiz(quiz);
    }

    void updateQuiz(Quiz quiz) {
        mRepository.updateQuiz(quiz);
    }

    void deleteQuiz(long quizId) {
        mRepository.deleteQuiz(quizId);
    }

    void insertQuestions(List<Question> questions) {
        for (Question question : questions) {
            mRepository.insertQuestion(question);
        }
    }

//    void updateQuestions(long quizId) {
//        mRepository.updateQuestions(quizId);
//    }

    void insertTerms(List<Term> terms) {
        for (Term term : terms) {
            termRepository.insert(term);
        }
    }
}

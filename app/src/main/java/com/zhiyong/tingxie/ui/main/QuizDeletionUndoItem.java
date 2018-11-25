package com.zhiyong.tingxie.ui.main;

import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.QuizPinyin;

import java.util.Set;

/**
 * Backup rows of tables quiz, quiz_pinyin and questions when deleting quiz, to restore if required.
 */
public class QuizDeletionUndoItem {
    private Quiz quiz;
    private Set<QuizPinyin> quizPinyins;
    private Set<Question> questions;

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Set<QuizPinyin> getQuizPinyins() {
        return quizPinyins;
    }

    public void setQuizPinyins(Set<QuizPinyin> quizPinyins) {
        this.quizPinyins = quizPinyins;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }
}

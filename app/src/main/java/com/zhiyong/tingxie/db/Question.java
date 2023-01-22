package com.zhiyong.tingxie.db;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("quiz_id")},
        foreignKeys = @ForeignKey(entity = Quiz.class, parentColumns = "id",
                childColumns = "quiz_id", onDelete = CASCADE))
public class Question {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    private long timestamp;

    private long reset_time;

    private String pinyin_string;

    private boolean correct;

    private long quiz_id;

    public Question(@NonNull long timestamp, long reset_time,
                    @NonNull String pinyin_string, @NonNull boolean correct,
                    @NonNull long quiz_id) {
        this.timestamp = timestamp;
        this.reset_time = reset_time;
        this.pinyin_string = pinyin_string;
        this.correct = correct;
        this.quiz_id = quiz_id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    @NonNull
    public long getId() {
        return id;
    }

    @NonNull
    public long getTimestamp() {
        return timestamp;
    }

    @NonNull
    public long getReset_time() {
        return reset_time;
    }

    @NonNull
    public String getPinyin_string() {
        return pinyin_string;
    }

    @NonNull
    public boolean isCorrect() {
        return correct;
    }

    @NonNull
    public long getQuiz_id() {
        return quiz_id;
    }

    public static class QuestionBuilder {
        private long nestedTimestamp;
        private String nestedPinyinString;
        private boolean nestedCorrect;
        private long nestedQuizId;

        public QuestionBuilder timestamp(long timestamp) {
            this.nestedTimestamp = timestamp;
            return this;
        }

        public QuestionBuilder pinyinString(String pinyinString) {
            this.nestedPinyinString = pinyinString;
            return this;
        }

        public QuestionBuilder correct(boolean correct) {
            this.nestedCorrect = correct;
            return this;
        }

        public QuestionBuilder quizId(long quizId) {
            this.nestedQuizId = quizId;
            return this;
        }

        public Question build() {
            return new Question(nestedTimestamp, nestedTimestamp, nestedPinyinString, nestedCorrect,
                    nestedQuizId);
        }
    }
}

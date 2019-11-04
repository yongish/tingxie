/**
 * todo: May delete this because we do not directly access MySQL DB.
 */
package com.zhiyong.tingxie.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity()
public class Term {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    private long createdAt;

    private long deletedAt;

    private long quizId;

    private String userId;

    private String word;

    private String pinyin;

    private int timesCorrect;

    public Term(@NonNull long createdAt, @NonNull long deletedAt, @NonNull long quizId,
                @NonNull String userId, @NonNull String word, @NonNull String pinyin) {
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.quizId = quizId;
        this.userId = userId;
        this.word = word;
        this.pinyin = pinyin;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

//    public void setTimesCorrect(@NonNull long quizId, @NonNull String userId, @NonNull String word,
//                                )
}

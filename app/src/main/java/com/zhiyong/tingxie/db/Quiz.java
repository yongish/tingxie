package com.zhiyong.tingxie.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(indices = {@Index("id")})
public class Quiz {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private long id;

    // todo: Maybe consider encryption of local data.
    // For syncing with backend DB. Also in case multiple users use this phone.
    private String uid; // For syncing with backend DB. Also in case multiple users use this phone.

    private int date;

    private String title;

    private int totalTerms;

    private int notLearned;

    private int roundsCompleted;

    public Quiz(@NonNull int date, String uid) {
        this.date = date;
        title = "No title";
        this.uid = uid;
        totalTerms = 0;
        notLearned = 0;
        roundsCompleted = 0;
    }

    @Ignore
    public Quiz(long id, @NonNull int date, String title, int totalTerms, int notLearned,
                int roundsCompleted) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.totalTerms = totalTerms;
        this.notLearned = notLearned;
        this.roundsCompleted = roundsCompleted;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NonNull
    public int getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }
}

package com.zhiyong.tingxie.ui.main;

import android.os.Parcel;
import android.os.Parcelable;

public class QuizItem implements Parcelable {
    private long id;
    private int date;
    private String title;
    private int totalWords;
    private int notLearned;
    private int round;

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeInt(date);
        out.writeString(title);
        out.writeInt(totalWords);
        out.writeInt(notLearned);
        out.writeInt(round);
    }

    public static final Parcelable.Creator<QuizItem> CREATOR
            = new Parcelable.Creator<QuizItem>() {
        public QuizItem createFromParcel(Parcel in) {
            return new QuizItem(in);
        }

        public QuizItem[] newArray(int size) {
            return new QuizItem[size];
        }
    };

    private QuizItem(Parcel in) {
        id = in.readLong();
        date = in.readInt();
        title = in.readString();
        totalWords = in.readInt();
        notLearned = in.readInt();
        round = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public QuizItem(long id, int date, String title, int totalWords, int notLearned, int round) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.totalWords = totalWords;
        this.notLearned = notLearned;
        this.round = round;
    }

    public long getId() {
        return id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public int getNotLearned() {
        return notLearned;
    }

    public void setNotLearned(int notLearned) {
        this.notLearned = notLearned;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}

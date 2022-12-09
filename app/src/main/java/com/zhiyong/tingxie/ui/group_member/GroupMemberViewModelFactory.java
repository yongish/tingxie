package com.zhiyong.tingxie.ui.group_member;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.zhiyong.tingxie.ui.question.QuestionViewModel;

public class GroupMemberViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;
    private final long groupId;

    public GroupMemberViewModelFactory(Application application, long groupId) {
        mApplication = application;
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new QuestionViewModel(mApplication, groupId);
    }
}

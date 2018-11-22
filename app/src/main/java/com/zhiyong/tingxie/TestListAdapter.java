package com.zhiyong.tingxie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.TestViewHolder> {
    private final LayoutInflater mInflater;
    private List<Test> mTests;  // Cached copy of tests

    TestListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, viewGroup, false);
        return new TestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, int i) {
        if (mTests != null) {
            Test current = mTests.get(i);
            holder.testItemView.setText(String.valueOf(current.getDate()));
        } else {
            holder.testItemView.setText("No Date");
        }
    }

    void setTests(List<Test> tests) {
        mTests = tests;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mTests != null) return mTests.size();
        return 0;
    }

    class TestViewHolder extends RecyclerView.ViewHolder {
        private final TextView testItemView;

        private TestViewHolder(View itemView) {
            super(itemView);
            testItemView = itemView.findViewById(R.id.textView);
        }
    }
}

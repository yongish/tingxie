package com.zhiyong.tingxie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.TestViewHolder> {
    private final LayoutInflater mInflater;
//    private List<Test> mTests;  // Cached copy of tests
    private List<TestItem> mTests;  // Cached copy of tests

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
            // todo: TestItem list should be available here.
            // Construct in TestRepository.

//            Test current = mTests.get(i);
            Test current = new Test(mTests.get(i).getDate());
            holder.tvDate.setText(String.valueOf(current.getDate()));

//            holder.tvWordsLeft.setText(String.format(Locale.US,
//                    "%d/%d words left on %d round",
//                    current.getNotLearned(), current.getTotalWords(), current.getRound()));
        } else {
            holder.tvDate.setText("No Date");
//            holder.tvWordsLeft.setText("No info on progress.");
        }
    }

//    void setTests(List<Test> tests) {
//        mTests = tests;
//        notifyDataSetChanged();
//    }
    void setTests(List<TestItem> tests) {
        mTests = tests;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mTests != null) return mTests.size();
        return 0;
    }

    class TestViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvWordsLeft;

        private TestViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvWordsLeft = itemView.findViewById(R.id.tvWordsLeft);
        }
    }
}

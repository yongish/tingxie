package com.zhiyong.tingxie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder> {
    private final LayoutInflater mInflater;
    private List<QuizItem> mQuizItems;

    QuizListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, viewGroup, false);
        return new QuizViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QuizViewHolder holder, int i) {
        if (mQuizItems != null) {
            QuizItem current = mQuizItems.get(i);

            String displayDate = String.valueOf(current.getDate());
            SimpleDateFormat fromDB = new SimpleDateFormat("yyyymmdd");
            SimpleDateFormat newDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
            try {
                displayDate = newDateFormat.format(fromDB.parse(displayDate));
            } catch (ParseException e) {
                // todo: Error log API in future.
            }

            holder.tvDate.setText(displayDate);
            holder.tvWordsLeft.setText(String.format(Locale.US,
                    "%d/%d words left on %d round",
                    current.getNotLearned(), current.getTotalWords(), current.getRound()));
        } else {
            holder.tvDate.setText("No Date");
            holder.tvWordsLeft.setText("No info on progress.");
        }
    }

    void setQuizItems(List<QuizItem> quizItems) {
        mQuizItems = quizItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mQuizItems != null) return mQuizItems.size();
        return 0;
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvWordsLeft;

        private QuizViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvWordsLeft = itemView.findViewById(R.id.tvWordsLeft);
        }
    }
}

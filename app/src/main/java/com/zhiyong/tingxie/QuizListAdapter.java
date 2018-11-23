package com.zhiyong.tingxie;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder> {
    private final LayoutInflater mInflater;
    private List<QuizItem> mQuizItems;
    private final Context context;
    private final Calendar c = Calendar.getInstance();

    QuizListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, viewGroup, false);
        return new QuizViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QuizViewHolder holder, final int i) {
        if (mQuizItems != null) {
            final QuizItem current = mQuizItems.get(i);

            String displayDate = String.valueOf(current.getDate());
            SimpleDateFormat fromDB = new SimpleDateFormat("yyyyMMdd");
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

            holder.ivEditDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open date dialog. Pass in quiz ID.
                    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            QuizItem newItem = current;

                            int newIntDate = Integer.valueOf(String.valueOf(year) +
                                    String.format("%02d", ++month) + String.valueOf(dayOfMonth));
                            Log.d("newIntDate", String.valueOf(newIntDate));
                            newItem.setDate(newIntDate);
                            mQuizItems.set(i, newItem);
                            notifyDataSetChanged();
                        }
                    };
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog =
                            new DatePickerDialog(context, dateListener, year, month, day);
                    datePickerDialog.show();
                }
            });
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
        private final ImageView ivEditDate;

        private QuizViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvWordsLeft = itemView.findViewById(R.id.tvWordsLeft);
            ivEditDate = itemView.findViewById(R.id.ivEditDate);
        }
    }
}

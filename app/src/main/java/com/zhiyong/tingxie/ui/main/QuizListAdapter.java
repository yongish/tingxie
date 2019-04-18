package com.zhiyong.tingxie.ui.main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.Util;
import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.QuizPinyin;
import com.zhiyong.tingxie.ui.question.QuestionActivity;
import com.zhiyong.tingxie.ui.word.WordActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder> {

    public static final int WORD_ACTIVITY_REQUEST_CODE = 2;
    public static final int QUESTION_ACTIVITY_REQUEST_CODE = 3;
    public static final String EXTRA_QUIZ_ID = "com.zhiyong.tingxie.ui.main.extra.QUIZ_ID";

    private final Calendar c = Calendar.getInstance();
    private final LayoutInflater mInflater;
    private final Context context;

    private QuizViewModel viewModel;
    private RecyclerView recyclerView;

    private List<QuizItem> mQuizItems;

    // All question and quiz_pinyin rows (for undo deletes). May be suboptimal to get all rows, but
    // getting only question and quiz_pinyin is tricky and may be overengineering.
    private List<Question> mQuestions;
    private List<QuizPinyin> mQuizPinyins;

    QuizListAdapter(Context context, QuizViewModel viewModel, RecyclerView recyclerView) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.viewModel = viewModel;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_quiz, viewGroup, false);
        return new QuizViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final QuizViewHolder holder, final int i) {
        if (mQuizItems != null) {
            final QuizItem current = mQuizItems.get(i);

            String displayDate = String.valueOf(current.getDate());
            try {
                displayDate = Util.DISPLAY_FORMAT.format(Util.DB_FORMAT.parse(displayDate));
            } catch (ParseException e) {
                // todo: Error log API in future.
            }


            holder.tvDate.setText(displayDate);
            holder.tvWordsLeft.setText(String.format(Locale.US,
//                    "%d words",
//                    current.getTotalWords()));
                    "%d/%d remaining on round %d",
                    current.getNotLearned(), current.getTotalWords(), current.getRound()));

            holder.ivEditDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open date dialog. Pass in quiz ID and current date.
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

                    Date currDate;
                    try {
                        currDate = Util.DB_FORMAT.parse(String.valueOf(current.getDate()));
                        c.setTime(currDate);
                    } catch (ParseException e) {
                        // todo: Error logging API.
                    }

                    DialogFragment newFragment = DatePickerFragment.newInstance(
                            current.getId(),
                            c.get(Calendar.YEAR),
                            c.get(Calendar.MONTH),
                            c.get(Calendar.DAY_OF_MONTH)
                    );
                    newFragment.show(
                            ((FragmentActivity)context).getSupportFragmentManager(), "datePicker"
                    );
                }
            });
            holder.btnAddViewWords.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WordActivity.class);
                    intent.putExtra(EXTRA_QUIZ_ID, current.getId());
                    ((Activity) context).startActivityForResult(
                            intent, WORD_ACTIVITY_REQUEST_CODE
                    );
                }
            });
            holder.btnStartResume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (current.getTotalWords() > 0) {
                        Intent intent = new Intent(context, QuestionActivity.class);
                        intent.putExtra(EXTRA_QUIZ_ID, current.getId());
                        ((Activity) context).startActivityForResult(
                                intent, QUESTION_ACTIVITY_REQUEST_CODE
                        );
                    } else {
                        Toast.makeText(context, "No words in quiz", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemRemove(holder);
                }
            });
        } else {
            holder.tvDate.setText("No Date");
            holder.tvWordsLeft.setText("No info on progress.");
        }
    }

    void setQuizItems(List<QuizItem> quizItems, final RecyclerView mRecyclerView) {
        mQuizItems = quizItems;
        // Scroll to 1 position before next quiz.
        int scrollPosition = 0;
        Date today = new Date();
        for (int i = 1; i < quizItems.size(); i++) {
            try {
                Date quizDate = Util.DB_FORMAT.parse(String.valueOf(quizItems.get(i).getDate()));
                if (quizDate.compareTo(today) >= 0) {
                    scrollPosition = i - 1;
                    break;
                }
            } catch (ParseException e) {
                // todo: Error logging API.
            }
        }

        final int position = scrollPosition;
        Log.d("SCROLL POSITION: ", String.valueOf(scrollPosition));
        notifyDataSetChanged();
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(position);
            }
        });
    }

    void setQuestions(List<Question> questions) {
        mQuestions = questions;
    }

    void setQuizPinyins(List<QuizPinyin> quizPinyins) {
        mQuizPinyins = quizPinyins;
    }

    void onItemRemove(RecyclerView.ViewHolder viewHolder) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final QuizItem quizItem = mQuizItems.get(adapterPosition);
        // Get question and quiz_pinyin rows to be deleted (from all question and quiz_pinyin rows).
        final long quizId = quizItem.getId();

        Snackbar snackbar = Snackbar
                .make(recyclerView, "Removed quiz", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mQuizItems.add(adapterPosition, quizItem);
                        notifyItemInserted(adapterPosition);

                        // Reinsert deleted quiz, question, quiz_pinyin rows.
                        Quiz quiz = new Quiz(quizId, quizItem.getDate());
                        viewModel.insertQuiz(quiz);
                        viewModel.insertQuestions(getQuestionsOfQuiz(quizId));
                        viewModel.insertQuizPinyins(getQuizPinyinsOfQuiz(quizId));

                        recyclerView.scrollToPosition(adapterPosition);
                    }
                });
        snackbar.show();
        mQuizItems.remove(adapterPosition);
        viewModel.deleteQuiz(quizId);
        notifyItemRemoved(adapterPosition);
    }

    private List<Question> getQuestionsOfQuiz(long quizId) {
        List<Question> result = new ArrayList<>();
        for (Question question : mQuestions) {
            if (question.getId() == quizId) {
                result.add(question);
            }
        }
        return result;
    }

    private List<QuizPinyin> getQuizPinyinsOfQuiz(long quizId) {
        List<QuizPinyin> result = new ArrayList<>();
        for (QuizPinyin quizPinyin : mQuizPinyins) {
            if (quizPinyin.getQuiz_id() == quizId) {
                result.add(quizPinyin);
            }
        }
        return result;
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
        private final Button btnAddViewWords;
        private final Button btnStartResume;
        private final ImageView ivDelete;

        private QuizViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvWordsLeft = itemView.findViewById(R.id.tvWordsLeft);
            ivEditDate = itemView.findViewById(R.id.ivEditDate);
            btnAddViewWords = itemView.findViewById(R.id.btnAddViewWords);
            btnStartResume = itemView.findViewById(R.id.btnStartResume);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}

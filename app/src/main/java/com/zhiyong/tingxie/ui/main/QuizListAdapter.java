package com.zhiyong.tingxie.ui.main;

import static com.zhiyong.tingxie.ui.UserRoleKt.EXTRA_USER_ROLE;
import static com.zhiyong.tingxie.ui.question.RemoteQuestionActivity.EXTRA_QUIZ_ITEM;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.Util;
import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.QuizPinyin;
import com.zhiyong.tingxie.network.NetworkQuiz;
import com.zhiyong.tingxie.ui.UserRole;
import com.zhiyong.tingxie.ui.question.RemoteQuestionActivity;
import com.zhiyong.tingxie.ui.share.EnumQuizRole;
import com.zhiyong.tingxie.ui.share.ShareActivity;
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

    private final Calendar c = Calendar.getInstance();
    private final LayoutInflater mInflater;
    private final Context context;

    private List<NetworkQuiz> mQuizItems;

    private final QuizViewModel viewModel;
    private final RecyclerView recyclerView;

    // All question and quiz_pinyin rows (for undo deletes). May be suboptimal to get
    // all rows, but
    // getting only question and quiz_pinyin is tricky and may be overengineering.
    private List<Question> mQuestions;
    private List<QuizPinyin> mQuizPinyins;

    QuizListAdapter(Context context, QuizViewModel viewModel,
                    RecyclerView recyclerView) {
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
    public void onBindViewHolder(final QuizViewHolder holder, int i) {
        final NetworkQuiz quizItem = mQuizItems.get(i);

        if (mQuizItems != null) {
            final NetworkQuiz current = mQuizItems.get(i);

            String displayDate = String.valueOf(current.getDate());
            try {
                displayDate =
                        Util.DISPLAY_FORMAT.format(Util.DB_FORMAT.parse(displayDate));
            } catch (ParseException e) {
                // todo: Error log API in future.
            }


            holder.tvDate.setText(displayDate);
            holder.etTitle.setText(current.getTitle());
            holder.tvWordsLeft.setText(String.format(Locale.US,
//                    "%d words",
//                    current.getTotalWords()));
                    "%d/%d remaining on round %d",
                    current.getNumNotCorrect(), current.getNumWords(),
                    current.getRound()));

            holder.ivEditDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open date dialog. Pass in quiz ID and current date.
                    DatePickerDialog.OnDateSetListener dateListener =
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int month, int dayOfMonth) {
                                    int newIntDate = Integer.valueOf(year +
                                            String.format("%02d", ++month) + dayOfMonth);
                                    Log.d("newIntDate", String.valueOf(newIntDate));
                                    current.setDate(newIntDate);
                                    mQuizItems.set(holder.getAdapterPosition(), current);
                                    notifyDataSetChanged();
                                }
                            };

                    Date currDate;
                    try {
                        currDate =
                                Util.DB_FORMAT.parse(String.valueOf(current.getDate()));
                        c.setTime(currDate);
                    } catch (ParseException e) {
                        // todo: Error logging API.
                    }

                    DialogFragment newFragment = DatePickerFragment.newInstance(
//                            current.getId(),
                            current,
                            i,
                            c.get(Calendar.YEAR),
                            c.get(Calendar.MONTH),
                            c.get(Calendar.DAY_OF_MONTH)
                    );
                    newFragment.show(
                            ((FragmentActivity) context).getSupportFragmentManager(),
                            "datePicker"
                    );
                }
            });
            holder.etTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        NetworkQuiz newItem = current;
                        newItem.setTitle(v.getText().toString());
                        mQuizItems.set(holder.getAdapterPosition(), newItem);
                        notifyDataSetChanged();

                        viewModel.updateQuiz(newItem);

                        InputMethodManager inputManager = (InputMethodManager)
                                context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.toggleSoftInput(0, 0);

                        holder.etTitle.clearFocus();
                    }
                    return false;
                }
            });
            holder.btnAddViewWords.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WordActivity.class);
                    intent.putExtra(EXTRA_QUIZ_ITEM, quizItem);
                    ((Activity) context).startActivityForResult(
                            intent, WORD_ACTIVITY_REQUEST_CODE
                    );
                }
            });
            holder.btnStartResume.setOnClickListener(v -> {
                if (current.getNumWords() > 0) {
                    Intent intent = new Intent(context,
                            RemoteQuestionActivity.class);
                    intent.putExtra(EXTRA_QUIZ_ITEM, quizItem);
                    ((Activity) context).startActivityForResult(
                            intent, QUESTION_ACTIVITY_REQUEST_CODE
                    );
                } else {
                    Toast.makeText(context, "No words in quiz",
                            Toast.LENGTH_SHORT).show();
                }
            });
            holder.ivDelete.setOnClickListener(v -> onItemRemove(holder));
            holder.ivShare.setOnClickListener(v -> {
                Intent intent = new Intent(context, ShareActivity.class);
                intent.putExtra(EXTRA_USER_ROLE, new UserRole(quizItem.getQuizId(),
                        EnumQuizRole.valueOf(quizItem.getRole())));
                context.startActivity(intent);
            });
        } else {
            holder.tvDate.setText("No Date");
            holder.tvWordsLeft.setText("No info on progress.");
        }
    }

    void setQuizItems(List<NetworkQuiz> quizItems, final RecyclerView mRecyclerView) {
        mQuizItems = quizItems;
        // Scroll to 1 position before next quiz.
        int scrollPosition = 0;
        Date today = new Date();
        for (int i = 1; i < quizItems.size(); i++) {
            try {
                Date quizDate =
                        Util.DB_FORMAT.parse(String.valueOf(quizItems.get(i).getDate()));
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
        mRecyclerView.post(() -> mRecyclerView.smoothScrollToPosition(position));
    }

    void addQuizItem(NetworkQuiz newQuizItem, RecyclerView recyclerView) {
        mQuizItems.add(0, newQuizItem);
        notifyItemInserted(0);
        recyclerView.post(() -> recyclerView.smoothScrollToPosition(0));
    }

    void replaceQuizItem(NetworkQuiz newQuizItem, RecyclerView recyclerView, int i) {
        mQuizItems.set(i, newQuizItem);
        notifyItemChanged(i);
        recyclerView.post(() -> recyclerView.smoothScrollToPosition(i));
    }

    void setQuestions(List<Question> questions) {
        mQuestions = questions;
    }

    void setQuizPinyins(List<QuizPinyin> quizPinyins) {
        mQuizPinyins = quizPinyins;
    }

    void onItemRemove(RecyclerView.ViewHolder viewHolder) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final NetworkQuiz quizItem = mQuizItems.get(adapterPosition);
        final long quizId = quizItem.getQuizId();

        Snackbar snackbar = Snackbar
                .make(recyclerView, "Removed quiz", Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> {
                    mQuizItems.add(adapterPosition, quizItem);
                    notifyItemInserted(adapterPosition);
                    recyclerView.scrollToPosition(adapterPosition);
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        viewModel.deleteQuiz(quizId);
                    }
                });
        snackbar.show();

        mQuizItems.remove(adapterPosition);
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

    @Override
    public int getItemCount() {
        if (mQuizItems != null) return mQuizItems.size();
        return 0;
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final EditText etTitle;
        private final TextView tvWordsLeft;
        private final ImageView ivEditDate;
        private final Button btnAddViewWords;
        private final Button btnStartResume;
        private final ImageView ivDelete;
        private final ImageView ivShare;

        private QuizViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            etTitle = itemView.findViewById(R.id.etTitle);
            tvWordsLeft = itemView.findViewById(R.id.tvWordsLeft);
            ivEditDate = itemView.findViewById(R.id.ivEditDate);
            btnAddViewWords = itemView.findViewById(R.id.btnAddViewWords);
            btnStartResume = itemView.findViewById(R.id.btnStartResume);
            ivDelete = itemView.findViewById(R.id.btnDetails);
            ivShare = itemView.findViewById(R.id.ivShare);
        }
    }
}

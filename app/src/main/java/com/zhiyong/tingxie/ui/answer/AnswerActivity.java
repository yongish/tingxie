package com.zhiyong.tingxie.ui.answer;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.ui.main.MainActivity;
import com.zhiyong.tingxie.ui.question.QuestionActivity;

import static com.zhiyong.tingxie.ui.main.QuizListAdapter.EXTRA_QUIZ_ID;
import static com.zhiyong.tingxie.ui.question.QuestionActivity.EXTRA_PINYIN_STRING;
import static com.zhiyong.tingxie.ui.question.QuestionActivity.EXTRA_REMAINING_QUESTION_COUNT;
import static com.zhiyong.tingxie.ui.question.QuestionActivity.EXTRA_WORDS_STRING;

public class AnswerActivity extends AppCompatActivity {

    private AnswerViewModel mAnswerViewModel;
    private TextView tvAnswerWords;
    private Button btnAnswerCorrect;
    private Button btnAnswerWrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        mAnswerViewModel = ViewModelProviders.of(this).get(AnswerViewModel.class);

        final int quizId = getIntent().getIntExtra(EXTRA_QUIZ_ID, -1);
        final String wordsString = getIntent().getStringExtra(EXTRA_WORDS_STRING);
        final String pinyinString = getIntent().getStringExtra(EXTRA_PINYIN_STRING);

        tvAnswerWords = findViewById(R.id.tvAnswerWords);
        tvAnswerWords.setText(wordsString);

        btnAnswerCorrect = findViewById(R.id.btnAnswerCorrect);
        final Question.QuestionBuilder questionBuilder = new Question.QuestionBuilder()
                .timestamp(System.currentTimeMillis())
                .pinyinString(pinyinString)
                .quizId(quizId);
        final Intent intentQuestion = new Intent(getApplicationContext(), QuestionActivity.class);
        intentQuestion.putExtra(EXTRA_QUIZ_ID, quizId);
        btnAnswerCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Insert new question with boolean correct.
                Question question = questionBuilder.correct(true).build();
                mAnswerViewModel.insertQuestion(question);

                // Go to Completed Alert Dialog or QuestionActivity.
                // Was this the last word in current round?
                int remainingQuestionCount = getIntent().getIntExtra(EXTRA_REMAINING_QUESTION_COUNT, -1);
                if (remainingQuestionCount < 2) {
                    new AlertDialog.Builder(AnswerActivity.this)
                            .setTitle("Round Completed.")
                            .setMessage("Great. You completed a round with all questions correct.")
                            .setPositiveButton("Next round", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                                    intent.putExtra(EXTRA_QUIZ_ID, quizId);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Main menu", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            })
                            .show();
                } else {
                    startActivity(intentQuestion);
                }
            }
        });
        btnAnswerWrong = findViewById(R.id.btnAnswerWrong);
        btnAnswerWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Insert new question with boolean wrong.
                Question question = questionBuilder.correct(false).build();
                mAnswerViewModel.insertQuestion(question);

                // Go to QuestionActivity.
                startActivity(intentQuestion);
            }
        });
    }
}

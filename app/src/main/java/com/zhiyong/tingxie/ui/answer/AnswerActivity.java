package com.zhiyong.tingxie.ui.answer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.ui.question.QuestionActivity;

import static com.zhiyong.tingxie.ui.main.QuizListAdapter.EXTRA_QUIZ_ID;
import static com.zhiyong.tingxie.ui.question.QuestionActivity.EXTRA_PINYIN_STRING;
import static com.zhiyong.tingxie.ui.question.QuestionActivity.EXTRA_WORDS_STRING;

public class AnswerActivity extends AppCompatActivity {

    private TextView tvAnswerWords;
    private Button btnAnswerCorrect;
    private Button btnAnswerWrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

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
        btnAnswerCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Insert new question with boolean correct.
                Question question = questionBuilder.correct(true).build();

                // Go to CompletedActivity or QuestionActivity.
                // Was this the last word in current round?

            }
        });
        btnAnswerWrong = findViewById(R.id.btnAnswerWrong);
        btnAnswerWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Insert new question with boolean wrong.
                Question question = questionBuilder.correct(false).build();

                // Go to QuestionActivity.
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                intent.putExtra(EXTRA_QUIZ_ID, quizId);
                startActivity(intent);
            }
        });
    }
}

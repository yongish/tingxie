package com.zhiyong.tingxie.ui.answer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.ui.main.MainActivity;
import com.zhiyong.tingxie.ui.main.QuizItem;
import com.zhiyong.tingxie.ui.question.QuestionActivity;
import com.zhiyong.tingxie.ui.word.WordItem;

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

        QuizItem quizItem = getIntent().getParcelableExtra("quiz");

        final String wordsString = getIntent().getStringExtra(EXTRA_WORDS_STRING);
        final String pinyinString = getIntent().getStringExtra(EXTRA_PINYIN_STRING);

        tvAnswerWords = findViewById(R.id.tvAnswerWords);
        tvAnswerWords.setText(wordsString);
        tvAnswerWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://baike.baidu.com/item/" + wordsString;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        btnAnswerCorrect = findViewById(R.id.btnAnswerCorrect);
        long ts = System.currentTimeMillis();
        final Question.QuestionBuilder questionBuilder = new Question.QuestionBuilder()
                .timestamp(ts)
                .pinyinString(pinyinString)
                .quizId(quizItem.getId());
        final Intent intentQuestion = new Intent(getApplicationContext(),
                QuestionActivity.class);
        intentQuestion.putExtra("quiz", quizItem);
        btnAnswerCorrect.setOnClickListener(v -> {
            // Insert new question with boolean correct.
            Question question = questionBuilder.correct(true).build();
            mAnswerViewModel.insertQuestion(question);

            // todo: UPDATE notLearned.
            quizItem.setNotLearned(quizItem.getNotLearned() - 1);
            mAnswerViewModel.updateWordItem(new WordItem(quizItem.getId(), wordsString, pinyinString, true));

            // Go to Completed Alert Dialog or QuestionActivity.
            // Was this the last word in current round?
            int remainingQuestionCount = getIntent()
                    .getIntExtra(EXTRA_REMAINING_QUESTION_COUNT, -1);
            Log.d("REMAINING_QN", String.valueOf(remainingQuestionCount));
            if (remainingQuestionCount < 2) {
                // todo: UPDATE ROUND.
                quizItem.setRound(quizItem.getRound() + 1);
                quizItem.setNotLearned(quizItem.getTotalWords());
                mAnswerViewModel.resetAsked(quizItem.getId());

                // todo: Show AnswerActivity in future.
                new AlertDialog.Builder(AnswerActivity.this)
                        .setTitle("Round Completed.")
                        .setMessage("Great. You completed a round with all questions correct.")
                        .setPositiveButton("Next round", (dialog, which) -> {
                            Intent intent = new Intent(getApplicationContext(),
                                    QuestionActivity.class);
                            intent.putExtra("quiz", quizItem);
                            startActivity(intent);
                        })
                        .setNegativeButton("Main menu", (dialog, which) ->
                                startActivity(new Intent(getApplicationContext(),
                                        MainActivity.class))
                        )
                        .show();
            } else {
                Toast.makeText(AnswerActivity.this, "Good",
                        Toast.LENGTH_SHORT).show();
                startActivity(intentQuestion);
            }

            mAnswerViewModel.updateQuiz(quizItem);
        });
        btnAnswerWrong = findViewById(R.id.btnAnswerWrong);
        btnAnswerWrong.setOnClickListener(v -> {
            // Insert new question with boolean wrong.
            Question question = questionBuilder.correct(false).build();
            mAnswerViewModel.insertQuestion(question);

            mAnswerViewModel.updateWordItem(new WordItem(quizItem.getId(), wordsString, pinyinString, false));

            Toast.makeText(AnswerActivity.this, "Keep going.",
                    Toast.LENGTH_SHORT).show();

            // Go to QuestionActivity.
            startActivity(intentQuestion);
        });
    }
}

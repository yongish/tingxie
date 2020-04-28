package com.zhiyong.tingxie.ui.question;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.ui.answer.AnswerActivity;
import com.zhiyong.tingxie.ui.main.MainActivity;
import com.zhiyong.tingxie.ui.main.QuizItem;
import com.zhiyong.tingxie.ui.word.WordItem;

import java.util.Locale;

public class QuestionActivity extends AppCompatActivity {

    public static final String EXTRA_WORDS_STRING = "com.zhiyong.tingxie.ui.question.extra.WORDS_STRING";
    public static final String EXTRA_PINYIN_STRING = "com.zhiyong.tingxie.ui.question.extra.PINYIN_STRING";
    public static final String EXTRA_REMAINING_QUESTION_COUNT = "com.zhiyong.tingxie.ui.question.extra.REMAINING_QUESTION_COUNT";

    private TextToSpeech textToSpeech;
    private QuestionViewModel mQuestionViewModel;
    private ImageView ivPlay;
    private Button btnShowAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> {
            // todo: Scroll to same quiz.
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        // From AnswerActivity.
        QuizItem quizItem = getIntent().getParcelableExtra("quiz");

        ivPlay = findViewById(R.id.ivPlay);
        btnShowAnswer = findViewById(R.id.btnShowAnswer);
        mQuestionViewModel = ViewModelProviders
                .of(this, new QuestionViewModelFactory(this.getApplication(), quizItem.getId()))
                .get(QuestionViewModel.class);
        mQuestionViewModel.getRemainingQuestions().observe(this, wordItems -> {
            if (wordItems != null && wordItems.size() > 0) {
                final WordItem wordItem = wordItems.get(0);
                final String wordString = wordItem.getWordString();
                final String pinyinString = wordItem.getPinyinString();
                if (textToSpeech == null) {
                    textToSpeech = new TextToSpeech(getApplicationContext(), arg0 -> {
                        if (arg0 == TextToSpeech.SUCCESS) {
                            textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                            textToSpeech.speak(wordString, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    });
                } else {
                    textToSpeech.speak(wordString, TextToSpeech.QUEUE_FLUSH, null);
                }

                ivPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textToSpeech.speak(wordString, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });

                btnShowAnswer.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
                    StringBuilder sb = new StringBuilder();
                    for (WordItem item : wordItems) {
                        if (item.getPinyinString().equals(pinyinString)) {
                            sb.append("\n");
                            sb.append(item.getWordString());
                        }
                    }
                    intent.putExtra(EXTRA_WORDS_STRING, sb.deleteCharAt(0).toString());
                    intent.putExtra(EXTRA_PINYIN_STRING, pinyinString);

                    intent.putExtra("quiz", quizItem);
                    intent.putExtra(EXTRA_REMAINING_QUESTION_COUNT, wordItems.size());
                    startActivity(intent);
                });
            } else {
                // todo: There should always be questions. Should log this issue.
                // For now, select a random question.
                Log.e("NO_QUESTIONS", "onChanged: ");
                Toast.makeText(getApplicationContext(),
                        "Error. Please contact Zhiyong by Facebook or email if you see this.",
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                // todo: Is quizId necessary?
//                    intent.putExtra(EXTRA_QUIZ_ID, quizId);
                startActivity(intent);
            }
        });
    }
}

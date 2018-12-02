package com.zhiyong.tingxie.ui.question;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.ui.answer.AnswerActivity;
import com.zhiyong.tingxie.ui.main.MainActivity;
import com.zhiyong.tingxie.ui.word.WordItem;

import java.util.List;
import java.util.Locale;

import static com.zhiyong.tingxie.ui.main.QuizListAdapter.EXTRA_QUIZ_ID;

public class QuestionActivity extends AppCompatActivity {

    public static final String EXTRA_WORDS_STRING = "com.zhiyong.tingxie.ui.question.extra.PINYIN_STRING";

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: Scroll to same quiz.
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final int quizId = getIntent().getIntExtra(EXTRA_QUIZ_ID, -1);

        ivPlay = findViewById(R.id.ivPlay);
        btnShowAnswer = findViewById(R.id.btnShowAnswer);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                }
            }
        });
        mQuestionViewModel = ViewModelProviders
                .of(this, new QuestionViewModelFactory(this.getApplication(), quizId))
                .get(QuestionViewModel.class);
        mQuestionViewModel.getRandomQuestion().observe(this, new Observer<List<WordItem>>() {
            @Override
            public void onChanged(@Nullable final List<WordItem> wordItems) {
                ivPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textToSpeech.speak(wordItems.get(0).getWordString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                });

                btnShowAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
                        StringBuilder sb = new StringBuilder();
                        for (WordItem item : wordItems) {
                            sb.append("\n");
                            sb.append(item.getWordString());
                        }
                        intent.putExtra(EXTRA_WORDS_STRING, sb.deleteCharAt(0).toString());

                        intent.putExtra(EXTRA_QUIZ_ID, quizId);
                        startActivity(intent);
                    }
                });

            }
        });
    }

}

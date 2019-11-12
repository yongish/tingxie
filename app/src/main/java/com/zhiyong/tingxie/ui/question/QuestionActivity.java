package com.zhiyong.tingxie.ui.question;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.db.Term;
import com.zhiyong.tingxie.ui.answer.AnswerActivity;
import com.zhiyong.tingxie.ui.main.MainActivity;

import java.util.List;
import java.util.Locale;

import static com.zhiyong.tingxie.ui.main.QuizListAdapter.EXTRA_QUIZ_ID;

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

        final long quizId = getIntent().getLongExtra(EXTRA_QUIZ_ID, -1);

        ivPlay = findViewById(R.id.ivPlay);
        btnShowAnswer = findViewById(R.id.btnShowAnswer);

        mQuestionViewModel = ViewModelProviders
                .of(this, new QuestionViewModelFactory(this.getApplication(), quizId))
                .get(QuestionViewModel.class);
        mQuestionViewModel.getRemainingQuestions().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(@Nullable final List<Term> wordItems) {
                if (wordItems != null && wordItems.size() > 0) {
                    final Term wordItem = wordItems.get(0);
                    final String wordString = wordItem.getWord();
                    final String pinyinString = wordItem.getPinyin();
                    if (textToSpeech == null) {
                        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int arg0) {
                                if (arg0 == TextToSpeech.SUCCESS) {
                                    textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                                    textToSpeech.speak(wordString, TextToSpeech.QUEUE_FLUSH, null);
                                }
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

                    btnShowAnswer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
                            StringBuilder sb = new StringBuilder();
                            for (Term item : wordItems) {
                                if (item.getPinyin().equals(pinyinString)) {
                                    sb.append("\n");
                                    sb.append(item.getWord());
                                }
                            }
                            intent.putExtra(EXTRA_WORDS_STRING, sb.deleteCharAt(0).toString());
                            intent.putExtra(EXTRA_PINYIN_STRING, pinyinString);
                            intent.putExtra(EXTRA_QUIZ_ID, quizId);
                            intent.putExtra(EXTRA_REMAINING_QUESTION_COUNT, wordItems.size());
                            startActivity(intent);
                        }
                    });
                } else {
                    // todo: There should always be questions. Should log this issue.
                    // For now, select a random question.
                    Log.e("NO_QUESTIONS", "onChanged: ");
                    Toast.makeText(getApplicationContext(),
                            "Error. Please contact Zhiyong by Facebook or email if you see this.",
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra(EXTRA_QUIZ_ID, quizId);
                    startActivity(intent);
                }
            }
        });
    }
}

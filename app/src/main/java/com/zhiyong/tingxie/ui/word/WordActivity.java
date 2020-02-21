package com.zhiyong.tingxie.ui.word;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.ui.main.MainActivity;
import com.zhiyong.tingxie.ui.main.QuizItem;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WordActivity extends AppCompatActivity {

    private WordViewModel mWordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        List<String> words = new ArrayList<>();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("words.txt")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                words.add(mLine.trim());
            }
            Log.i("TAG", "onCreate: ");
        } catch (IOException e) {
            Log.e("TAG", "onCreate: load cedict.json error", e);
        }

        toolbar.setNavigationOnClickListener((View v) -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });

        QuizItem quizItem = getIntent().getParcelableExtra("quiz");
        long quizId = quizItem.getId();

        final AutoCompleteTextView textView = findViewById(R.id.autoCompleteTextView1);
        textView.setThreshold(1);

        ArrayAdapter<String> arrayadapter = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                words.toArray(new String[0])
        );

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {

            textView.setInputType(InputType.TYPE_CLASS_TEXT);
            if (textView.getParent() != null) {
                ((ViewGroup) textView.getParent()).removeView(textView); // <- fix
            }
            textView.setAdapter(arrayadapter);

            FrameLayout container = new FrameLayout(WordActivity.this);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = params.rightMargin =
                    getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            textView.setLayoutParams(params);
            container.addView(textView);

            AlertDialog dialog = new AlertDialog.Builder(WordActivity.this)
                    .setTitle("Add Chinese words or phase")
                    .setMessage("No punctuation allowed.")
                    .setView(container)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Only keep Chinese characters and punctuation.
                            String inputWord = textView.getText().toString().replaceAll("\\s", "");
                            String pinyin = null;
                            try {
                                // Use Pinyin4J as backup.
                                HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
                                format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
                                format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
                                // Workaround as toHanyuPinyinString "separate" argument leaves last 2 strings concatenated.
                                String temp = PinyinHelper.toHanYuPinyinString(inputWord + "a", format, " ", true);
                                pinyin = temp.substring(0, temp.length() - 1);
                                // todo: Remove lookupPinyin when sure.
//                                    pinyin = new Util().lookupPinyin(inputWord);
                            } catch (Exception e) {
                                // todo: Log to API.
                                Toast.makeText(WordActivity.this, "ERROR in local pinyin lookup", Toast.LENGTH_LONG).show();
                            }

                            if (pinyin != null && pinyin.length() > 0) {
                                // Add word to current quizId.
                                mWordViewModel.addWord(quizId, inputWord, pinyin);
                                mWordViewModel.updateQuestions(quizId);

                                // Update totalWords. Reset notLearned and round.
                                int totalWords = quizItem.getTotalWords() + 1;
                                quizItem.setTotalWords(totalWords);
                                quizItem.setNotLearned(totalWords);
                                quizItem.setRound(1);

                                mWordViewModel.updateQuiz(quizItem);
                            } else {
                                Toast.makeText(WordActivity.this, "ERROR in pinyin lookup", Toast.LENGTH_LONG).show();
                                dialog.cancel();
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.show();
        });

        mWordViewModel = ViewModelProviders
                .of(this, new WordViewModelFactory(this.getApplication(), quizId))
                .get(WordViewModel.class);
        final RecyclerView recyclerView = findViewById(R.id.recyclerview_word);
        final WordListAdapter adapter = new WordListAdapter(
                this, mWordViewModel, recyclerView, quizItem
        );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final TextView emptyView = findViewById(R.id.empty_view);
        mWordViewModel.getWordItemsOfQuiz().observe(this, new Observer<List<WordItem>>() {
            @Override
            public void onChanged(@Nullable List<WordItem> wordItems) {
                adapter.setWordItems(wordItems);
                if (wordItems == null || wordItems.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    emptyView.setVisibility(View.INVISIBLE);
                }
            }
        });


        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.onItemRemove(viewHolder);
            }
        });
        helper.attachToRecyclerView(recyclerView);
    }
}

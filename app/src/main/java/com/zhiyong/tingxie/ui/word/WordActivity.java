package com.zhiyong.tingxie.ui.word;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.github.promeg.pinyinhelper.Pinyin;
import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.ui.main.MainActivity;

import java.util.List;

import static com.zhiyong.tingxie.ui.main.QuizListAdapter.EXTRA_QUIZ_ID;

public class WordActivity extends AppCompatActivity {

    private WordViewModel mWordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        final int quizId = getIntent().getIntExtra(EXTRA_QUIZ_ID, -1);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(WordActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                FrameLayout container = new FrameLayout(WordActivity.this);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = params.rightMargin =
                        getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                input.setLayoutParams(params);
                container.addView(input);
                new AlertDialog.Builder(WordActivity.this)
                        .setTitle("Add Chinese words or phase")
                        .setView(container)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Only keep Chinese characters and punctuation.
                                String inputWord = input.getText().toString().replaceAll("\\s", "");
                                StringBuilder sb = new StringBuilder();
                                StringBuilder sbPinyin = new StringBuilder();
                                for (char c : inputWord.toCharArray()) {
                                    if (Pinyin.isChinese(c)) {
                                        sb.append(c);
                                        sbPinyin.append(Pinyin.toPinyin(c));
                                    }
                                }

                                String word = sb.toString();
                                String pinyin = sbPinyin.toString();

                                // Add word to current quizId.
                                mWordViewModel.addWord(quizId, word, pinyin);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.recyclerview_word);
        final WordListAdapter adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWordViewModel = ViewModelProviders
                .of(this, new WordViewModelFactory(this.getApplication(), quizId))
                .get(WordViewModel.class);
        mWordViewModel.getWordItemsOfQuiz().observe(this, new Observer<List<WordItem>>() {
            @Override
            public void onChanged(@Nullable List<WordItem> wordItems) {
                adapter.setWordItems(wordItems);
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
                adapter.onItemRemove(viewHolder, recyclerView, mWordViewModel);
            }
        });
        helper.attachToRecyclerView(recyclerView);
    }
}

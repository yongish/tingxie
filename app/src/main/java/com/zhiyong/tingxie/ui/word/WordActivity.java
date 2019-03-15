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
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.Util;
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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        final long quizId = getIntent().getLongExtra(EXTRA_QUIZ_ID, -1);

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
                        .setMessage("No punctuation allowed.")
                        .setView(container)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Only keep Chinese characters and punctuation.
                                String inputWord = input.getText().toString().replaceAll("\\s", "");
                                String pinyin = null;
                                try {
                                    pinyin = new Util().lookupPinyin(inputWord);
                                } catch (Exception e) {
                                    // todo: Log to API.
                                    Toast.makeText(WordActivity.this, "ERROR in pinyin lookup", Toast.LENGTH_LONG).show();
                                }
                                if (pinyin.length() > 0) {
                                    // Add word to current quizId.
                                    mWordViewModel.addWord(quizId, inputWord, pinyin);


                                    // todo: Reset correct counters of all quiz words to 0.


                                } else {
                                    dialog.cancel();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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

        final TextView emptyView = findViewById(R.id.empty_view);
        mWordViewModel = ViewModelProviders
                .of(this, new WordViewModelFactory(this.getApplication(), quizId))
                .get(WordViewModel.class);
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
                adapter.onItemRemove(viewHolder, recyclerView, mWordViewModel);
            }
        });
        helper.attachToRecyclerView(recyclerView);
    }
}

package com.zhiyong.tingxie.ui.word;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.db.QuizPinyin;
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview_word);
        final WordListAdapter adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        int quizId = getIntent().getIntExtra(EXTRA_QUIZ_ID, -1);
        mWordViewModel = ViewModelProviders
                .of(this, new WordViewModelFactory(this.getApplication(), quizId))
                .get(WordViewModel.class);
        mWordViewModel.getWordItemsOfQuiz().observe(this, new Observer<List<WordItem>>() {
            @Override
            public void onChanged(@Nullable List<WordItem> wordItems) {
                adapter.setWordItems(wordItems);

                // Delete here?
            }
        });


        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                        int position = viewHolder.getAdapterPosition();
                        WordItem wordItem = adapter.getWordItemAtPosition(position);

                        mWordViewModel.deleteWord(new QuizPinyin(wordItem.getQuizId(), wordItem.getPinyinId()));
                    }
                }
        );
        helper.attachToRecyclerView(recyclerView);

    }
}

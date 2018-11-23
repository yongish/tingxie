package com.zhiyong.tingxie;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.zhiyong.tingxie.db.Quiz;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_QUIZ_ACTIVITY_REQUEST_CODE = 1;

    private QuizViewModel mQuizViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewQuizActivity.class);
                startActivityForResult(intent, NEW_QUIZ_ACTIVITY_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final QuizListAdapter adapter = new QuizListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mQuizViewModel = ViewModelProviders.of(this).get(QuizViewModel.class);
        mQuizViewModel.getAllQuizItems().observe(this, new Observer<List<QuizItem>>() {
            @Override
            public void onChanged(@Nullable List<QuizItem> quizItems) {
                adapter.setQuizItems(quizItems);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_QUIZ_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Quiz quiz = new Quiz(data.getIntExtra(NewQuizActivity.EXTRA_REPLY, 0));
            mQuizViewModel.insert(quiz);
        } else {
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG)
                    .show();
        }
    }
}

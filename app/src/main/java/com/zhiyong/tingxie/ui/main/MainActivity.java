package com.zhiyong.tingxie.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.ui.friend.FriendActivity;
import com.zhiyong.tingxie.ui.hsk.buttons.HskButtonsActivity;
import com.zhiyong.tingxie.ui.login.LoginActivity;


public class MainActivity extends AppCompatActivity {

    private QuizViewModel mQuizViewModel;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View v) -> {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(),
                    getString(R.string.datepicker));
        });

        recyclerView = findViewById(R.id.recyclerview_main);

        final TextView emptyView = findViewById(R.id.empty_view);
        mQuizViewModel = ViewModelProviders.of(this).get(QuizViewModel.class);

        final QuizListAdapter adapter = new QuizListAdapter(this, mQuizViewModel,
                recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mQuizViewModel.getAllQuizItems().observe(this, quizItems -> {
            adapter.setQuizItems(quizItems, recyclerView);
            mQuizViewModel.refreshQuizzes(quizItems);
            if (quizItems.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.INVISIBLE);
            }
        });

//        mQuizViewModel.getEventNetworkError().observe(this, isNetworkError -> {
//            if (isNetworkError) onNetworkError();
//        });
//        mQuizViewModel.getQuizzes().observe(this, quizzes -> {
//            adapter.setQuizItems();
//        });

        /* todo: getAllQuestions() and getAllQuizPinyins() here may be unnecessary.
         */
//        mQuizViewModel.getAllQuestions().observe(this, questions ->
//                adapter.setQuestions(questions)
//        );
//        mQuizViewModel.getAllQuizPinyins().observe(this, quizPinyins ->
//                adapter.setQuizPinyins(quizPinyins)
//        );

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.onItemRemove(viewHolder);
            }
        });
        helper.attachToRecyclerView(recyclerView);
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

    public void openFriends(MenuItem item) {
        startActivity(new Intent(MainActivity.this, FriendActivity.class));
    }

    public static Intent openSpeechSettingsHelper() {
        Intent intent = new Intent();
        intent.setAction("com.android.settings.TTS_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public void openSpeechSettings(MenuItem item) {
        startActivity(openSpeechSettingsHelper());
    }

    public void openHskLists(MenuItem item) {
        startActivity(new Intent(MainActivity.this, HskButtonsActivity.class));
    }

    public void processDatePickerResult(long quizId, int year, int month, int day) {
        int date = Integer.valueOf(year + String.format("%02d", ++month) +
                String.format("%02d", day));
        Quiz quiz = new Quiz(date);
        if (quizId != -1) {
            quiz.setId(quizId);
            mQuizViewModel.updateQuiz(quiz);
            return;
        }
        mQuizViewModel.insertQuiz(quiz);
    }

    public void openHelp(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        HelpDialogFragment fragment = HelpDialogFragment.newInstance();
        fragment.show(fm, "fragment_help");
    }

    public void logout(MenuItem item) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    startActivity(new Intent(MainActivity.this,
                            LoginActivity.class));
                    finish();
                });
    }

//    private void onNetworkError() {
//        if(!mQuizViewModel.isNetworkErrorShown().getValue()) {
//            Toast.makeText(this, "Network Error", Toast.LENGTH_LONG).show();
//            mQuizViewModel.onNetworkErrorShown();
//        }
//    }
}

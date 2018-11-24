package com.zhiyong.tingxie;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewQuizActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.zhiyong.tingxie.REPLY";

    private EditText mEditQuizView;
    private int year;
    private int month;
    private int dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_quiz);
        mEditQuizView = findViewById(R.id.edit_word);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditQuizView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    // todo: Consider utility function for this date formatting.
                    int date = Integer.valueOf(String.valueOf(year) +
                            String.format("%02d", ++month) + String.valueOf(dayOfMonth));
                    replyIntent.putExtra(EXTRA_REPLY, date);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), getString(R.string.datepicker));
    }

    public void processDatePickerResult(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        this.year = year;
        this.month = month;
        this.dayOfMonth = day;
        mEditQuizView.setText(Util.DISPLAY_FORMAT.format(c.getTime()));
    }
}

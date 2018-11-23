package com.zhiyong.tingxie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewQuizActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.zhiyong.tingxie.REPLY";

    private EditText mEditQuizView;

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
                    int date = Integer.valueOf(mEditQuizView.getText().toString());
                    replyIntent.putExtra(EXTRA_REPLY, date);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}

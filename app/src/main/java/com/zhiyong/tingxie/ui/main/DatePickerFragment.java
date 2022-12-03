package com.zhiyong.tingxie.ui.main;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Optional;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

//    private long quizId;
    private QuizItem quizItem;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        try {
            quizItem = getArguments().getParcelable("quizItem")
//            quizId = getArguments().getLong("quizId");
        } catch (NullPointerException e) {
//            quizId = -1;
        }
        int year;
        try {
            year = getArguments().getInt("year", c.get(Calendar.YEAR));
        } catch (NullPointerException e) {
            year = c.get(Calendar.YEAR);
        }
        int month;
        try {
            month = getArguments().getInt("month");
        } catch (NullPointerException e) {
            month = c.get(Calendar.MONTH);
        }
        int day;
        try {
            day = getArguments().getInt("day");
        } catch (NullPointerException e) {
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

//    static DatePickerFragment newInstance(long quizId, int year, int month, int day) {
    static DatePickerFragment newInstance(QuizItem quizItem, int position, int year, int month, int day) {
        DatePickerFragment f = new DatePickerFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
//        args.putLong("quizId", quizId);
        args.putParcelable("quizItem", quizItem);

        // STOPPED HERE. FINISH UP POSITION.
        args.putInt("position", position);

        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        MainActivity activity = (MainActivity) getActivity();
//        activity.processDatePickerResult(quizId, year, month, dayOfMonth);
        activity.processDatePickerResult(Optional.of(quizItem), year, month, dayOfMonth);
    }
}

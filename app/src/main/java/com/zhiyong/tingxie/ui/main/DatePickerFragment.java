package com.zhiyong.tingxie.ui.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private long quizId;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        try {
            quizId = getArguments().getLong("quizId");
        } catch (NullPointerException e) {
            quizId = -1;
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

    static DatePickerFragment newInstance(long quizId, int year, int month, int day) {
        DatePickerFragment f = new DatePickerFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putLong("quizId", quizId);
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        MainActivity activity = (MainActivity) getActivity();
        activity.processDatePickerResult(quizId, year, month, dayOfMonth);
    }
}

package com.zhiyong.tingxie;

import android.widget.DatePicker;

public interface DateSelectedListener {
    void onDateSelected(DatePicker view, int year, int month, int day);
}

package com.zhiyong.tingxie;

import java.text.SimpleDateFormat;

public class Util {
    public final static SimpleDateFormat DB_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public final static SimpleDateFormat DISPLAY_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy");
    private Util() {
        throw new AssertionError();
    }
}

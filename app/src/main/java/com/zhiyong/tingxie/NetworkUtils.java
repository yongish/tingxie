package com.zhiyong.tingxie;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    static Element getPinyin(String url) {
        Element result = null;
        try {
            result = Jsoup.connect(url).get().getElementById("pinyin");
        } catch (IOException e) {
            // todo: Log to error API.
            Log.d("getPinyinWrapper: ", e.getMessage());
        }
        return result;
    }
}

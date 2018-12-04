package com.zhiyong.tingxie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Util {
    public static final SimpleDateFormat DB_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat DISPLAY_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy");

    private static final String BAIDU_DICT_URL_LOOKUP = "https://dict.baidu.com/s?wd=";

    public static String lookupPinyin(String input) {
        String initialUrl = BAIDU_DICT_URL_LOOKUP + input;
        Element pinyinWrapper = getPinyinWrapper(initialUrl);
        String result;
        if (pinyinWrapper == null) {
            StringBuilder sb = new StringBuilder();
            for (char c : input.toCharArray()) {
                sb.append(processPinyin(getPinyinWrapper(BAIDU_DICT_URL_LOOKUP + String.valueOf(c))));
            }
            result = sb.toString();
        } else {
            result = processPinyin(pinyinWrapper);
        }
        return result;
    }

    private static Element getPinyinWrapper(String url) {
        Element result = null;
        try {
            result = Jsoup.connect(url).get().getElementById("pinyin");
        } catch (IOException e) {
            // todo: Log to error API.
            Log.d("getPinyinWrapper: ", e.getMessage());
        }
        return result;
    }

    private static String processPinyin(Element pinyinWrapper) {
        return pinyinWrapper.selectFirst("b").text().replace("[", "")
                .replace("]", "").trim();
    }

    private Util() {
        throw new AssertionError();
    }
}

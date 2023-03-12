package com.zhiyong.tingxie;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

public class Util {
    public static final SimpleDateFormat DB_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat DISPLAY_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy");

    private static final String BAIDU_DICT_URL_LOOKUP = "https://dict.baidu.com/s?wd=";

    public String lookupPinyin(String input) throws ExecutionException, InterruptedException {
        String initialUrl = BAIDU_DICT_URL_LOOKUP + input;
//        Element pinyinWrapper = getPinyinWrapper(initialUrl);
        Element pinyinWrapper = new GetPinyin().execute(initialUrl).get();
        String result;
        if (pinyinWrapper == null) {
            StringBuilder sb = new StringBuilder();
            for (char c : input.toCharArray()) {
//                sb.append(processPinyin(getPinyinWrapper(BAIDU_DICT_URL_LOOKUP + String.valueOf(c))));
                sb.append(processPinyin(
                        new GetPinyin().execute(BAIDU_DICT_URL_LOOKUP + c).get()
                ));
            }
            result = sb.toString();
        } else {
            result = processPinyin(pinyinWrapper);
        }
        return result;
    }

//    private static Element getPinyinWrapper(String url) {
//        Element result = null;
//        try {
//            result = Jsoup.connect(url).get().getElementById("pinyin");
//        } catch (IOException e) {
//            // todo: Log to error API.
//            Log.d("getPinyinWrapper: ", e.getMessage());
//        }
//        return result;
//    }

    public class GetPinyin extends AsyncTask<String, Void, Element> {

        @Override
        protected Element doInBackground(String... strings) {
            return NetworkUtils.getPinyin(strings[0]);
        }

        @Override
        protected void onPostExecute(Element s) {
            super.onPostExecute(s);
        }
    }

    private static String processPinyin(Element pinyinWrapper) {
        return pinyinWrapper.selectFirst("b").text().replace("[", "")
                .replace("]", "").trim();
    }

//    private Util() {
//        throw new AssertionError();
//    }
}

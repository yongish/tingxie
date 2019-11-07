package com.zhiyong.tingxie;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;

import okhttp3.OkHttpClient;

public class NetworkUtils {
    private static OkHttpClient client = new OkHttpClient();

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

//    public static LiveData<List<Term>> getTerms(String uid, String quizId) {
//        uid = "Um1QB4JIqNb61MYXVdc99ZUidkt2";
//        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjVkY2U3ZTQxYWRkMTIxYjg2ZWQ0MDRiODRkYTc1NzM5NDY3ZWQyYmMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGluZ3hpZS1iMWFjYiIsImF1ZCI6InRpbmd4aWUtYjFhY2IiLCJhdXRoX3RpbWUiOjE1NzE1OTg2MzEsInVzZXJfaWQiOiJVbTFRQjRKSXFOYjYxTVlYVmRjOTlaVWlka3QyIiwic3ViIjoiVW0xUUI0SklxTmI2MU1ZWFZkYzk5WlVpZGt0MiIsImlhdCI6MTU3MjU4Nzc4NywiZXhwIjoxNTcyNTkxMzg3LCJlbWFpbCI6Inlvbmdpc2hAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsieW9uZ2lzaEBnbWFpbC5jb20iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.NTRmPzMaIdShfSCwp3-rpluMBbL8juNF8j4z85AVzcUK86NhN4KQsTliphD2Af4QLEzk7gTdFNEDUbdORerObcg0vlkkUOuUghGtn3loN4oDoOMtyFre0PrDnqEIsHbdPqYboz3raVLes2YRTEr-pSaRHtW1xlyOn23gKefx2oxqyIMS2KLmesiEWFbXOn3U-DvIkAtgbWv1A1z5DAzUT9xTCHZH9aIrn8ZupROokIKHtKiXgmcAib0uG4Cxprh7mlu6wkfhExSuskyQpm7EtE2dw70-7Iyy1uIwa1rMkvJEPrJ5IHBSjzXaL6QazGUwWzTG27RKikICaKX0xyTHtg";
//        Request request = new Request.Builder()
//                .header("Authorization", "Bearer " + token)
//                .url("https://localhost:8443/term/" + uid)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            MutableLiveData<List<Term>> terms = new MutableLiveData<>();
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                terms = null;
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                terms.setValue(response.body());
//            }
//        });
//        return terms;
//    }
}

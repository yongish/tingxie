package com.zhiyong.tingxie.ui.word;

import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.zhiyong.tingxie.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermRepository {

    private static TermRepository termRepository;
    private static FirebaseAuth auth;
    private static String uid;

    public static TermRepository getInstance(SharedPreferences pref) {
        if (termRepository == null) {
            termRepository = new TermRepository();
            auth = FirebaseAuth.getInstance();
            uid = pref.getString("uid", null);
        }
        return termRepository;
    }

    private TermApi termApi;

    public TermRepository() {
        termApi = RetrofitService.createService(TermApi.class);
    }

    public MutableLiveData<TermResponse> getTerms() {
        final MutableLiveData<TermResponse> terms = new MutableLiveData<>();
        if (uid == null) {
            throw new IllegalStateException("uid is null.");
        }

        termApi.getTermsList(uid).enqueue(new Callback<TermResponse>() {
            @Override
            public void onResponse(Call<TermResponse> call, Response<TermResponse> response) {
                if (response.isSuccessful()) {
                    terms.setValue(response.body());
                } else if (response.code() == 401){
                    // todo: Refresh token and try again.
                } else {
                    throw new IllegalStateException("Error in getting terms of uid: " + uid);
                }
            }

            @Override
            public void onFailure(Call<TermResponse> call, Throwable t) {
                terms.setValue(null);
            }
        });
        return terms;
    }
}

package com.zhiyong.tingxie.ui.word;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zhiyong.tingxie.db.Term;

import java.util.List;

public class TermResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("terms")
    @Expose
    private List<Term> terms;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }
}

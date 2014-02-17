package com.cstewart.android.muzeigram.controller;

import android.app.Activity;
import android.os.Bundle;

import com.cstewart.android.muzeigram.MuzeiGramApplication;

public class MuzeiGramActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MuzeiGramApplication.get(this).inject(this);
    }
}

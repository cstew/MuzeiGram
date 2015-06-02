package com.cstewart.android.muzeigram.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cstewart.android.muzeigram.MuzeiGramApplication;

public class MuzeiGramActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MuzeiGramApplication.get(this).inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            // I'm cheating here in all Activities and just calling finish.
            // At least the code is in one place.
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

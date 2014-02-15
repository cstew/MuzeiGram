package com.cstewart.android.muzeigram.controller;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.cstewart.android.muzeigram.R;
import com.cstewart.android.muzeigram.data.Settings;
import com.cstewart.android.muzeigram.data.UpdateInterval;

import java.util.Arrays;

public class InstagramSettingsActivity extends Activity {

    private Button mAuthorizeButton;
    private Spinner mUpdateIntervalSpinner;

    private Settings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_settings);

        mSettings = new Settings(this);

        mAuthorizeButton = (Button) findViewById(R.id.activity_instagram_settings_authorize);
        mAuthorizeButton.setOnClickListener(mAuthorizeClickListener);

        mUpdateIntervalSpinner = (Spinner) findViewById(R.id.activity_instagram_settings_interval_spinner);
        mUpdateIntervalSpinner.setOnItemSelectedListener(mUpdateIntervalSelected);
        setupUpdateIntervalAdapter();
    }

    private void setupUpdateIntervalAdapter() {
        UpdateInterval[] intervals = UpdateInterval.values();
        UpdateIntervalAdapter adapter = new UpdateIntervalAdapter(this, intervals);
        mUpdateIntervalSpinner.setAdapter(adapter);

        UpdateInterval currentInterval = mSettings.getUpdateInterval();
        int currentPosition = Arrays.asList(intervals).indexOf(currentInterval);
        mUpdateIntervalSpinner.setSelection(currentPosition);
    }

    private AdapterView.OnItemSelectedListener mUpdateIntervalSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            UpdateIntervalAdapter adapter = (UpdateIntervalAdapter) mUpdateIntervalSpinner.getAdapter();
            UpdateInterval updateInterval = adapter.getItem(position);
            mSettings.setUpdateInterval(updateInterval);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private static class UpdateIntervalAdapter extends ArrayAdapter<UpdateInterval> {

        public UpdateIntervalAdapter(Context context, UpdateInterval[] updateIntervals) {
            super(context, android.R.layout.simple_spinner_item, updateIntervals);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            return updateText(view, position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            return updateText(view, position);
        }

        private View updateText(View view, int position) {
            UpdateInterval updateInterval = getItem(position);
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getContext().getString(updateInterval.getNameResId()));
            return view;
        }
    }

    private View.OnClickListener mAuthorizeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(InstagramAuthorizeActivity.newIntent(InstagramSettingsActivity.this));
        }
    };
}

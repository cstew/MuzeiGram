package com.cstewart.android.muzeigram.controller;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.cstewart.android.muzeigram.R;
import com.cstewart.android.muzeigram.data.FeedType;
import com.cstewart.android.muzeigram.data.Settings;
import com.cstewart.android.muzeigram.data.UpdateInterval;
import com.google.android.apps.muzei.api.MuzeiArtSource;
import com.google.android.apps.muzei.api.internal.ProtocolConstants;

import java.util.Arrays;

public class InstagramSettingsActivity extends Activity {

    private Button mAuthorizeButton;
    private Spinner mFeedTypeSpinner;
    private Spinner mUpdateIntervalSpinner;

    private Settings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_settings);

        mSettings = new Settings(this);

        mAuthorizeButton = (Button) findViewById(R.id.activity_instagram_settings_authorize);
        mAuthorizeButton.setOnClickListener(mAuthorizeClickListener);

        mFeedTypeSpinner = (Spinner) findViewById(R.id.activity_instagram_settings_feed_type_spinner);
        mFeedTypeSpinner.setOnItemSelectedListener(mFeedTypeSelected);
        setupFeedTypeAdapter();

        mUpdateIntervalSpinner = (Spinner) findViewById(R.id.activity_instagram_settings_interval_spinner);
        mUpdateIntervalSpinner.setOnItemSelectedListener(mUpdateIntervalSelected);
        setupUpdateIntervalAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendUpdate();
    }

    private void setupFeedTypeAdapter() {
        FeedType[] feedTypes = FeedType.values();
        FeedTypeAdapter adapter = new FeedTypeAdapter(this, feedTypes);
        mFeedTypeSpinner.setAdapter(adapter);

        FeedType currentFeedType = mSettings.getFeedType();
        int currentPosition = Arrays.asList(feedTypes).indexOf(currentFeedType);
        mFeedTypeSpinner.setSelection(currentPosition);
    }

    private void setupUpdateIntervalAdapter() {
        UpdateInterval[] intervals = UpdateInterval.values();
        UpdateIntervalAdapter adapter = new UpdateIntervalAdapter(this, intervals);
        mUpdateIntervalSpinner.setAdapter(adapter);

        UpdateInterval currentInterval = mSettings.getUpdateInterval();
        int currentPosition = Arrays.asList(intervals).indexOf(currentInterval);
        mUpdateIntervalSpinner.setSelection(currentPosition);
    }

    private void sendUpdate() {
        // TODO: Is there a better way to do this?
        Intent updateIntent = new Intent(ProtocolConstants.ACTION_HANDLE_COMMAND)
                .setComponent(new ComponentName(this, InstagramRemoteArtSource.class))
                .setData(Uri.fromParts("muzeicommand",
                        Integer.toString(MuzeiArtSource.BUILTIN_COMMAND_ID_NEXT_ARTWORK), null))
                .putExtra(ProtocolConstants.EXTRA_COMMAND_ID, MuzeiArtSource.BUILTIN_COMMAND_ID_NEXT_ARTWORK)
                .putExtra(ProtocolConstants.EXTRA_SCHEDULED, true);
        startService(updateIntent);
    }

    private AdapterView.OnItemSelectedListener mFeedTypeSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            FeedTypeAdapter adapter = (FeedTypeAdapter) mFeedTypeSpinner.getAdapter();
            FeedType feedType = adapter.getItem(position);
            mSettings.setFeedType(feedType);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

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

    private abstract static class SettingAdapter<T> extends ArrayAdapter<T> {

        protected abstract void updateTitle(TextView textView, T item);

        public SettingAdapter(Context context, T[] items) {
            super(context, android.R.layout.simple_spinner_item, items);
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
            T item = getItem(position);
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            updateTitle(textView, item);
            return view;
        }
    }

    private static class FeedTypeAdapter extends SettingAdapter<FeedType> {

        public FeedTypeAdapter(Context context, FeedType[] items) {
            super(context, items);
        }

        @Override
        protected void updateTitle(TextView textView, FeedType item) {
            textView.setText(getContext().getString(item.getTitleResId()));
        }
    }

    private static class UpdateIntervalAdapter extends SettingAdapter<UpdateInterval> {

        public UpdateIntervalAdapter(Context context, UpdateInterval[] items) {
            super(context, items);
        }

        @Override
        protected void updateTitle(TextView textView, UpdateInterval item) {
            textView.setText(getContext().getString(item.getNameResId()));
        }
    }

    private View.OnClickListener mAuthorizeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(InstagramAuthorizeActivity.newIntent(InstagramSettingsActivity.this));
        }
    };
}

package com.cstewart.android.muzeigram.controller.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.cstewart.android.muzeigram.R;
import com.cstewart.android.muzeigram.controller.InstagramAuthorizeActivity;
import com.cstewart.android.muzeigram.controller.InstagramRemoteArtSource;
import com.cstewart.android.muzeigram.controller.MuzeiGramActivity;
import com.cstewart.android.muzeigram.data.settings.FeedType;
import com.cstewart.android.muzeigram.data.settings.Settings;
import com.cstewart.android.muzeigram.data.settings.UpdateInterval;
import com.google.android.apps.muzei.api.MuzeiArtSource;
import com.google.android.apps.muzei.api.internal.ProtocolConstants;

import java.util.Arrays;

import javax.inject.Inject;

public class InstagramSettingsActivity extends MuzeiGramActivity {

    private static final String TAG_ACCOUNTS_DIALOG = "AccountsDialog";

    @Inject Settings mSettings;

    private Button mAuthorizeButton;
    private Spinner mFeedTypeSpinner;
    private Spinner mUpdateIntervalSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_settings);

        mAuthorizeButton = (Button) findViewById(R.id.activity_instagram_settings_authorize);
        mAuthorizeButton.setOnClickListener(mAuthorizeClickListener);

        mFeedTypeSpinner = (Spinner) findViewById(R.id.activity_instagram_settings_feed_type_spinner);
        mFeedTypeSpinner.setOnItemSelectedListener(mFeedTypeSelected);
        setupFeedTypeAdapter();

        mUpdateIntervalSpinner = (Spinner) findViewById(R.id.activity_instagram_settings_interval_spinner);
        mUpdateIntervalSpinner.setOnItemSelectedListener(mUpdateIntervalSelected);


        Button usersButton = (Button) findViewById(R.id.activity_instagram_settings_users);
        usersButton.setOnClickListener(mUsersClickListener);

        setupUpdateIntervalAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateAuthorizeUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendUpdate();
    }

    private void updateAuthorizeUI() {
        boolean authorized = !TextUtils.isEmpty(mSettings.getInstagramToken());

        mAuthorizeButton.setText(authorized?
                R.string.activity_instagram_settings_authorize_set
                : R.string.activity_instagram_settings_authorize);
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

    private View.OnClickListener mUsersClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            UserAccountsAlertDialogFragment accountsDialogFragment = UserAccountsAlertDialogFragment.newInstance();
            accountsDialogFragment.show(getFragmentManager(), TAG_ACCOUNTS_DIALOG);
        }
    };
}

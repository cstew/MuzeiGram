package com.cstewart.android.muzeigram.controller.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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

    @Inject Settings mSettings;

    private View mDetailContainer;
    private Button mAuthorizeButton;
    private Spinner mFeedTypeSpinner;
    private TextView mCustomUserTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_settings);

        mDetailContainer = findViewById(R.id.activity_instagram_settings_detail_container);

        mAuthorizeButton = (Button) findViewById(R.id.activity_instagram_settings_authorize);
        mAuthorizeButton.setOnClickListener(mAuthorizeClickListener);

        mFeedTypeSpinner = (Spinner) findViewById(R.id.activity_instagram_settings_feed_type_spinner);
        mFeedTypeSpinner.setOnItemSelectedListener(mFeedTypeSelected);
        setupFeedTypeAdapter();

        mCustomUserTextView = (TextView) findViewById(R.id.activity_instagram_settings_custom_users);
        mCustomUserTextView.setOnClickListener(mCustomUserAccountClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendUpdate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (!isAuthorized()) {
            return false;
        }

        getMenuInflater().inflate(R.menu.activity_instagram_settings, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int menuId = mSettings.getUpdateInterval().getMenuResId();
        if (menuId != 0) {
            MenuItem item = menu.findItem(menuId);
            if (item != null) {
                item.setChecked(true);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        UpdateInterval interval = UpdateInterval.fromMenuResId(item.getItemId());
        if (interval != null) {
            mSettings.setUpdateInterval(interval);
            invalidateOptionsMenu();
            return true;
        }

        switch (item.getItemId()) {

            case R.id.action_remove_access:
                mSettings.saveInstagramToken(null);
                sendUpdate();
                updateUI();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        boolean isAuthorized = isAuthorized();

        mDetailContainer.setVisibility(isAuthorized? View.VISIBLE : View.GONE);
        mAuthorizeButton.setVisibility(isAuthorized? View.GONE : View.VISIBLE);

        FeedType feedType = mSettings.getFeedType();
        boolean isCustomType = (feedType == FeedType.CUSTOM);

        mCustomUserTextView.setVisibility(isCustomType? View.VISIBLE : View.GONE);

        String users = mSettings.getUserCollection().toString();
        if (TextUtils.isEmpty(users)) {
            users = getString(R.string.activity_instagram_settings_no_users);
        }
        mCustomUserTextView.setText(users);

        invalidateOptionsMenu();
    }

    private boolean isAuthorized() {
        return !TextUtils.isEmpty(mSettings.getInstagramToken());
    }

    private void setupFeedTypeAdapter() {
        FeedType[] feedTypes = FeedType.values();
        FeedTypeAdapter adapter = new FeedTypeAdapter(this, feedTypes);
        mFeedTypeSpinner.setAdapter(adapter);

        FeedType currentFeedType = mSettings.getFeedType();
        int currentPosition = Arrays.asList(feedTypes).indexOf(currentFeedType);
        mFeedTypeSpinner.setSelection(currentPosition);
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

            updateUI();
            sendUpdate();
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

    private View.OnClickListener mAuthorizeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(InstagramAuthorizeActivity.newIntent(InstagramSettingsActivity.this));
        }
    };

    private View.OnClickListener mCustomUserAccountClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent userChooser = InstagramUserChooserActivity.newIntent(InstagramSettingsActivity.this);
            startActivity(userChooser);
        }
    };

}

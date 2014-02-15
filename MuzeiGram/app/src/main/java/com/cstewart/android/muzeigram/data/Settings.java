package com.cstewart.android.muzeigram.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {

    private static final String KEY_TOKEN = "token";
    private static final String KEY_UPDATE_INTERVAL = "updateInterval";

    private SharedPreferences mSharedPreferences;

    public Settings(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveInstagramToken(String token) {
        mSharedPreferences.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getInstagramToken() {
        return mSharedPreferences.getString(KEY_TOKEN, null);
    }

    public UpdateInterval getUpdateInterval() {
        int seconds = mSharedPreferences.getInt(KEY_UPDATE_INTERVAL, 0);
        return UpdateInterval.from(seconds);
    }

    public void setUpdateInterval(UpdateInterval updateInterval) {
        mSharedPreferences.edit().putInt(KEY_UPDATE_INTERVAL, updateInterval.getMilliseconds()).apply();
    }

}

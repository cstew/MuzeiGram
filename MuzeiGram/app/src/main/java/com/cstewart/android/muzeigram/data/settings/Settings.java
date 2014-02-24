package com.cstewart.android.muzeigram.data.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class Settings {

    private static final String KEY_TOKEN = "token";
    private static final String KEY_FEED_TYPE = "feedType";
    private static final String KEY_UPDATE_INTERVAL = "updateInterval";
    private static final String KEY_INSTAGRAM_USERS = "InstagramUsers";

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

    public FeedType getFeedType() {
        int feedTypeIndex = mSharedPreferences.getInt(KEY_FEED_TYPE, -1);
        return FeedType.from(feedTypeIndex);
    }

    public void setFeedType(FeedType feedType) {
        mSharedPreferences.edit().putInt(KEY_FEED_TYPE, feedType.getIndex()).apply();
    }

    public UpdateInterval getUpdateInterval() {
        int seconds = mSharedPreferences.getInt(KEY_UPDATE_INTERVAL, 0);
        return UpdateInterval.from(seconds);
    }

    public void setUpdateInterval(UpdateInterval updateInterval) {
        mSharedPreferences.edit().putInt(KEY_UPDATE_INTERVAL, updateInterval.getMilliseconds()).apply();
    }

    public InstagramUserCollection getUserCollection() {
//        String users = mSharedPreferences.getString(KEY_INSTAGRAM_USERS, null);
//        if (users == null) {
            return getDefaultUserCollection();
//        }
//
//        Gson gson = new Gson();
//        return gson.fromJson(users, InstagramUserCollection.class);
    }

    private InstagramUserCollection getDefaultUserCollection() {
        InstagramUserCollection collection = new InstagramUserCollection();
        collection.add("sserkan34", 829197244);
        collection.add("appletreeroad_luke", 11287532);
        collection.add("william_patino", 23516012);
        collection.add("curious2119", 157925);
        collection.add("daveyoder", 294330511);
        collection.add("skwii", 307146);
        collection.add("samhorine", 765701);
        collection.add("thiswildidea", 9868480);
        collection.add("swopes", 1624554);
        collection.add("dankhole", 166352);

        return collection;
    }

    private void setUserCollection(InstagramUserCollection userCollection) {
        String userJson = new Gson().toJson(userCollection);
        mSharedPreferences.edit().putString(KEY_INSTAGRAM_USERS, userJson).apply();
    }

}

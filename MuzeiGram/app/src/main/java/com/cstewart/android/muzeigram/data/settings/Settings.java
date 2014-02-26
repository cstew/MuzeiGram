package com.cstewart.android.muzeigram.data.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cstewart.android.muzeigram.data.instagram.InstagramUser;
import com.cstewart.android.muzeigram.data.instagram.InstagramUserCollection;
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
        String users = mSharedPreferences.getString(KEY_INSTAGRAM_USERS, null);
        if (users == null) {
            return getDefaultUserCollection();
        }

        Gson gson = new Gson();
        return gson.fromJson(users, InstagramUserCollection.class);
    }

    private InstagramUserCollection getDefaultUserCollection() {
        InstagramUserCollection collection = new InstagramUserCollection();
        collection.addUser(new InstagramUser(11287532, "appletreeroad_luke", "Luke Drew", "http://images.ak.instagram.com/profiles/profile_11287532_75sq_1364933678.jpg"));
        collection.addUser(new InstagramUser(23516012, "william_patino", "Will Patino", "http://images.ak.instagram.com/profiles/profile_23516012_75sq_1379415978.jpg"));
        collection.addUser(new InstagramUser(157925, "curious2119", "Tim Landis", "http://images.ak.instagram.com/profiles/profile_157925_75sq_1392511691.jpg"));
        collection.addUser(new InstagramUser(294330511, "daveyoder", "Dave Yoder", "http://images.ak.instagram.com/profiles/profile_294330511_75sq_1360293704.jpg"));
        collection.addUser(new InstagramUser(307146, "skwii", "Jussi Ulkuniemi", "http://images.ak.instagram.com/profiles/profile_307146_75sq_1374516761.jpg"));
        collection.addUser(new InstagramUser(765701, "samhorine", "Sam Horine", "http://images.ak.instagram.com/profiles/profile_765701_75sq_1380778905.jpg"));
        collection.addUser(new InstagramUser(9868480, "thiswildidea", "Theron Humphrey", "http://images.ak.instagram.com/profiles/profile_9868480_75sq_1364478484.jpg"));
        collection.addUser(new InstagramUser(1624554, "swopes", "Elise Swopes", "http://images.ak.instagram.com/profiles/profile_1624554_75sq_1390583720.jpg"));
        collection.addUser(new InstagramUser(166352, "dankhole", "dan cole", "http://images.ak.instagram.com/profiles/profile_166352_75sq_1364842626.jpg"));
        collection.addUser(new InstagramUser(28569248, "sserkan34", "Serkan Demirci", "http://images.ak.instagram.com/profiles/profile_28569248_75sq_1390288079.jpg"));
        return collection;
    }

    public void saveUserCollection(InstagramUserCollection userCollection) {
        String userJson = new Gson().toJson(userCollection);
        mSharedPreferences.edit().putString(KEY_INSTAGRAM_USERS, userJson).apply();
    }

}

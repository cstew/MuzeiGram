package com.cstewart.android.muzeigram.data.instagram;

import com.google.gson.annotations.SerializedName;

public class InstagramUser {

    @SerializedName("username")
    private String mUsername;

    @SerializedName("userId")
    private int mUserId;

    @SerializedName("full_name")
    private String mFullName;

    @SerializedName("profile_picture")
    private String mProfilePicture;

    public InstagramUser(int userId, String username, String fullName, String profilePicture) {
        mUserId = userId;
        mUsername = username;
        mFullName = fullName;
        mProfilePicture = profilePicture;
    }

    public String getUsername() {
        return mUsername;
    }

    public int getUserId() {
        return mUserId;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getProfilePicture() {
        return mProfilePicture;
    }

    @Override
    public String toString() {
        return mUsername;
    }
}
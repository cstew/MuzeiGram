package com.cstewart.android.muzeigram.data.settings;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class InstagramUserCollection {

    @SerializedName("users")
    private List<InstagramUser> mInstagramUsers = new ArrayList<InstagramUser>();

    public void add(String username, int userId) {
        mInstagramUsers.add(new InstagramUser(username, userId));
    }

    public List<InstagramUser> getInstagramUsers() {
        return mInstagramUsers;
    }

    public static class InstagramUser {

        @SerializedName("username")
        private String mUsername;

        @SerializedName("userId")
        private int mUserId;

        public InstagramUser(String username, int userId) {
            mUsername = username;
            mUserId = userId;
        }

        public String getUsername() {
            return mUsername;
        }

        public void setUsername(String username) {
            mUsername = username;
        }

        public int getUserId() {
            return mUserId;
        }

        public void setUserId(int userId) {
            mUserId = userId;
        }

        @Override
        public String toString() {
            return mUsername;
        }
    }

}

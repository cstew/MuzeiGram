package com.cstewart.android.muzeigram.data.instagram;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserSearchResponse {

    @SerializedName("data")
    private List<User> mUsers;

    public List<User> getUsers() {
        return mUsers;
    }

    public static class User {

        @SerializedName("username")
        private String mUsername;

        @SerializedName("id")
        private int mId;

        public String getUsername() {
            return mUsername;
        }

        public int getId() {
            return mId;
        }
    }
}

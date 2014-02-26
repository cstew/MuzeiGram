package com.cstewart.android.muzeigram.data.instagram;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserSearchResponse {

    @SerializedName("data")
    private List<InstagramUser> mUsers;

    public List<InstagramUser> getUsers() {
        return mUsers;
    }
}

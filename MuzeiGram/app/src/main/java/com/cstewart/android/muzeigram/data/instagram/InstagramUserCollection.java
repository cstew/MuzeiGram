package com.cstewart.android.muzeigram.data.instagram;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class InstagramUserCollection {

    @SerializedName("users")
    private List<InstagramUser> mInstagramUsers = new ArrayList<InstagramUser>();

    public List<InstagramUser> getInstagramUsers() {
        return mInstagramUsers;
    }

    public void addUser(InstagramUser user) {
        mInstagramUsers.add(user);
    }

    @Override
    public String toString() {
        return TextUtils.join(", ", mInstagramUsers);
    }

}

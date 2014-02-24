package com.cstewart.android.muzeigram.data.instagram;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MediaResponse {

    @SerializedName("data")
    private List<Media> mMedia;

    public List<Media> getMedia() {
        return mMedia;
    }
}

package com.cstewart.android.muzeigram.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chris on 2/15/14.
 */
public class MediaResponse {

    @SerializedName("data")
    private List<Media> mMedia;

    public List<Media> getMedia() {
        return mMedia;
    }
}

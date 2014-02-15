package com.cstewart.android.muzeigram.data;

import retrofit.http.GET;

/**
 * Created by chris on 2/15/14.
 */
public interface InstagramService {

    @GET("/v1/media/popular")
    MediaResponse getPopularPhotos();

}

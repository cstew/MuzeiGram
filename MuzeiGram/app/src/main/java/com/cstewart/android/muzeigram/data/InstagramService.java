package com.cstewart.android.muzeigram.data;

import retrofit.http.GET;

/**
 * Created by chris on 2/15/14.
 */
public interface InstagramService {

    @GET("/v1/media/popular")
    MediaResponse getPopularPhotos();

    @GET("/v1/users/self/feed")
    MediaResponse getFeedPhotos();

    @GET("/v1/users/self/media/liked")
    MediaResponse getLikedPhotos();

    @GET("/v1/users/self/media/recent/")
    MediaResponse getUsersPhotos();

}

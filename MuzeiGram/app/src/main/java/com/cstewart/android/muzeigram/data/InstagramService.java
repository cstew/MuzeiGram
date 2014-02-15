package com.cstewart.android.muzeigram.data;

import retrofit.http.GET;

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

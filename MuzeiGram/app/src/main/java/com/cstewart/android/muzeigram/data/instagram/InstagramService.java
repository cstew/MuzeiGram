package com.cstewart.android.muzeigram.data.instagram;


import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface InstagramService {

    @GET("/v1/media/popular")
    MediaResponse getPopularPhotos();

    @GET("/v1/users/self/feed")
    MediaResponse getFeedPhotos();

    @GET("/v1/users/self/media/liked")
    MediaResponse getLikedPhotos();

    @GET("/v1/users/self/media/recent/")
    MediaResponse getPersonalPhotos();

    @GET("/v1/users/{userId}/media/recent/")
    MediaResponse getUserPhotos(@Path("userId") long user);

    @GET("/v1/users/search")
    void getUserAccount(@Query("q") String username, Callback<UserSearchResponse> userResponseCallback);
}

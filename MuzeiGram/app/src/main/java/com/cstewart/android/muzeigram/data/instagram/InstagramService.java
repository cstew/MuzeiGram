package com.cstewart.android.muzeigram.data.instagram;


import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

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
    MediaResponse getUserPhotos(@Path("userId") int user);

    @GET("/v1/users/search?q={username}/")
    void getUserAccount(@Path("username") String username, Callback<UserSearchResponse> userResponseCallback);
}

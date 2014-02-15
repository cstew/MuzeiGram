package com.cstewart.android.muzeigram.controller;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.cstewart.android.muzeigram.data.Settings;
import com.cstewart.android.muzeigram.data.InstagramService;
import com.cstewart.android.muzeigram.data.Media;
import com.cstewart.android.muzeigram.data.MediaResponse;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import java.util.Random;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by chris on 2/15/14.
 */
public class InstagramRemoteArtSource extends RemoteMuzeiArtSource {
    private static final String TAG = "InstagramRemoteArtSource";

    private Settings mSettings;

    public InstagramRemoteArtSource() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSettings = new Settings(this);

        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }

    @Override
    protected void onTryUpdate(int i) throws RetryException {
        if (TextUtils.isEmpty(mSettings.getInstagramToken())) {
            setDefaultPicture();
        } else {
            loadPhotos();
        }
    }

    private void loadPhotos() throws RetryException {
        String currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : null;

        RestAdapter restAdapter = getRestAdapater(mSettings.getInstagramToken());
        InstagramService service = restAdapter.create(InstagramService.class);
        MediaResponse response = service.getPopularPhotos();

        if (response == null || response.getMedia() == null) {
            throw new RetryException();
        }

        if (response.getMedia().size() == 0) {
            Log.w(TAG, "No photos returned from API.");
            scheduleUpdate(System.currentTimeMillis() + mSettings.getUpdateInterval().getMilliseconds());
            return;
        }

        Random random = new Random();
        Media photo;
        String token;
        while (true) {
            photo = response.getMedia().get(random.nextInt(response.getMedia().size()));
            token = photo.getId();
            boolean isImage = photo.isImageType();
            boolean isDifferentImage = !TextUtils.equals(token, currentToken);
            if (response.getMedia().size() <= 1) {
                break;
            }

            if (isImage && isDifferentImage) {
                break;
            }
        }

        publishArtwork(new Artwork.Builder()
                .imageUri(Uri.parse(photo.getUrl()))
                .title(photo.getCaption())
                .byline(photo.getUsername())
                .token(token)
                .viewIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(photo.getLink())))
                .build());

        scheduleUpdate(System.currentTimeMillis() + mSettings.getUpdateInterval().getMilliseconds());
    }

    private void setDefaultPicture() {
        publishArtwork(new Artwork.Builder()
                .imageUri(Uri.parse("http://www.mrwallpaper.com/wallpapers/Waterfall-Landscape.jpg"))
                .title("Example image")
                .byline("Unknown person, c. 1980")
                .viewIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://example.com/imagedetails.html")))
                .build());
    }

    private RestAdapter getRestAdapater(final String accessToken) {
        return new RestAdapter.Builder()
                .setEndpoint("https://api.instagram.com")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade requestFacade) {
                        requestFacade.addEncodedQueryParam("access_token", accessToken);
                    }
                })
                .setErrorHandler(new ErrorHandler() {
                    @Override
                    public Throwable handleError(RetrofitError retrofitError) {
                        Log.e(TAG, "received error: " + retrofitError.toString());
                        Response response = retrofitError.getResponse();
                        if (response == null) {
                            return new RetryException();
                        }

                        int statusCode = response.getStatus();
                        if (retrofitError.isNetworkError()
                                || (500 <= statusCode && statusCode < 600)) {
                            return new RetryException();
                        }
                        scheduleUpdate(System.currentTimeMillis() + mSettings.getUpdateInterval().getMilliseconds());
                        return retrofitError;
                    }
                })
                .build();
    }
}

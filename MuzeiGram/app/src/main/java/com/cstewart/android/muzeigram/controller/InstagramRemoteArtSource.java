package com.cstewart.android.muzeigram.controller;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.cstewart.android.muzeigram.MuzeiGramApplication;
import com.cstewart.android.muzeigram.data.FeedType;
import com.cstewart.android.muzeigram.data.InstagramService;
import com.cstewart.android.muzeigram.data.Media;
import com.cstewart.android.muzeigram.data.MediaResponse;
import com.cstewart.android.muzeigram.data.Settings;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import java.util.Random;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class InstagramRemoteArtSource extends RemoteMuzeiArtSource {
    private static final String TAG = "InstagramRemoteArtSource";

    @Inject Settings mSettings;
    @Inject InstagramService mInstagramService;

    public InstagramRemoteArtSource() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MuzeiGramApplication.get(this).inject(this);

        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }

    @Override
    protected void onTryUpdate(int i) throws RetryException {
        if (TextUtils.isEmpty(mSettings.getInstagramToken())) {
            loadDefaultPicture();
            return;
        }

        try {
            loadPhotos();
        } catch (RetrofitError retrofitError) {
            Log.e(TAG, "Unable to load photos", retrofitError);
            Response response = retrofitError.getResponse();
            if (response == null) {
                throw new RetryException();
            }

            int statusCode = response.getStatus();
            if (retrofitError.isNetworkError()
                    || (500 <= statusCode && statusCode < 600)) {
                throw new RetryException();
            }
            scheduleNextUpdate();
        }
    }

    private void loadPhotos() throws RetryException, RetrofitError {
        String currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : null;
        MediaResponse response = getMedia();

        if (response == null || response.getMedia() == null) {
            throw new RetryException();
        }

        if (response.getMedia().size() == 0) {
            Log.w(TAG, "No photos returned from API.");
            scheduleNextUpdate();
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

        scheduleNextUpdate();
    }

    private MediaResponse getMedia() throws RetrofitError {
        FeedType feedType = mSettings.getFeedType();
        switch (feedType) {

            case FEED:
                return mInstagramService.getFeedPhotos();

            case LIKED:
                return mInstagramService.getLikedPhotos();

            case PERSONAL:
                return mInstagramService.getUsersPhotos();

            default:
            case POPULAR:
                return mInstagramService.getPopularPhotos();
        }
    }

    private void loadDefaultPicture() {
        publishArtwork(new Artwork.Builder()
                .imageUri(Uri.parse("http://distilleryimage9.s3.amazonaws.com/1b7292fa63ab11e38fe4125be5bdf6a9_8.jpg"))
                .build());
    }

    private void scheduleNextUpdate() {
        scheduleUpdate(System.currentTimeMillis() + mSettings.getUpdateInterval().getMilliseconds());
    }
}

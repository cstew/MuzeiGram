package com.cstewart.android.muzeigram.controller;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.cstewart.android.muzeigram.MuzeiGramApplication;
import com.cstewart.android.muzeigram.R;
import com.cstewart.android.muzeigram.data.instagram.InstagramService;
import com.cstewart.android.muzeigram.data.instagram.Media;
import com.cstewart.android.muzeigram.data.instagram.MediaResponse;
import com.cstewart.android.muzeigram.data.settings.FeedType;
import com.cstewart.android.muzeigram.data.settings.InstagramUserCollection;
import com.cstewart.android.muzeigram.data.settings.Settings;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import java.util.List;
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

        int description = R.string.app_description;
        if (!isUserAuthenticated()) {
            description = R.string.unauthorized_access_description;
        }
        setDescription(getString(description));
    }

    @Override
    protected void onTryUpdate(int i) throws RetryException {
        if (!isUserAuthenticated()) {
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

            case CUSTOM:
                return getCustomMediaResponse();

            case FEED:
                return mInstagramService.getFeedPhotos();

            case LIKED:
                return mInstagramService.getLikedPhotos();

            case PERSONAL:
                return mInstagramService.getPersonalPhotos();

            default:
            case POPULAR:
                return mInstagramService.getPopularPhotos();
        }
    }

    private MediaResponse getCustomMediaResponse() {
        InstagramUserCollection userCollection = mSettings.getUserCollection();
        List<InstagramUserCollection.InstagramUser> instagramUsers = userCollection.getInstagramUsers();
        int userSize = instagramUsers.size();

        Random random = new Random();
        InstagramUserCollection.InstagramUser randomUser = instagramUsers.get(random.nextInt(userSize));

        return mInstagramService.getUserPhotos(randomUser.getUserId());
    }

    private void loadDefaultPicture() {
        publishArtwork(new Artwork.Builder()
                .imageUri(Uri.parse("http://i.imgur.com/lCxL00e.jpg"))
                .title(getString(R.string.default_image_title))
                .byline(getString(R.string.default_image_byline))
                .token("initial")
                .build());
    }

    private void scheduleNextUpdate() {
        scheduleUpdate(System.currentTimeMillis() + mSettings.getUpdateInterval().getMilliseconds());
    }

    private boolean isUserAuthenticated() {
        return !TextUtils.isEmpty(mSettings.getInstagramToken());
    }
}

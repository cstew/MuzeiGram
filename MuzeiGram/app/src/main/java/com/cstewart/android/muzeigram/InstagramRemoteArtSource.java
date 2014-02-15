package com.cstewart.android.muzeigram;

import android.content.Intent;
import android.net.Uri;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

/**
 * Created by chris on 2/15/14.
 */
public class InstagramRemoteArtSource extends RemoteMuzeiArtSource {
    private static final String TAG = "InstagramRemoteArtSource";

    public InstagramRemoteArtSource() {
        super(TAG);
    }

    @Override
    protected void onTryUpdate(int i) throws RetryException {
        publishArtwork(new Artwork.Builder()
                .imageUri(Uri.parse("http://www.gwdhumanesociety.org/wp-content/uploads/2014/01/Instagram-Logo-1.jpg"))
                .title("Example image")
                .byline("Unknown person, c. 1980")
                .viewIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://example.com/imagedetails.html")))
                .build());
    }
}

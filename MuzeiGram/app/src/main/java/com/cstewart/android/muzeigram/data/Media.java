package com.cstewart.android.muzeigram.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chris on 2/15/14.
 */
public class Media {

    @SerializedName("id")
    private String mId;

    @SerializedName("type")
    private String mMediaType;

    @SerializedName("link")
    private String mLink;

    @SerializedName("images")
    private ImageContainer mImageContainer;

    @SerializedName("caption")
    private Caption mCaption;

    public String getId() {
        return mId;
    }

    public String getLink() {
        return mLink;
    }

    public String getUrl() {
        if (mImageContainer == null) {
            return null;
        }

        Image image = mImageContainer.getImage();
        if (image == null) {
            return null;
        }

        return image.getUrl();
    }

    public String getCaption() {
        if (mCaption == null) {
            return "";
        }

        return mCaption.getText();
    }

    public String getUsername() {
        if (mCaption == null) {
            return "";
        }

        User user = mCaption.getUser();
        if (user == null) {
            return "";
        }

        return user.getUsername();
    }

    public boolean isImageType() {
        return "image".equals(mMediaType);
    }

    public static class ImageContainer {

        @SerializedName("standard_resolution")
        private Image mImage;

        public Image getImage() {
            return mImage;
        }
    }

    public static class Image {

        @SerializedName("url")
        private String mUrl;

        public String getUrl() {
            return mUrl;
        }
    }

    public class Caption {

        @SerializedName("text")
        private String mText;

        @SerializedName("from")
        private User mUser;

        public String getText() {
            return mText;
        }

        public User getUser() {
            return mUser;
        }
    }

    public class User {

        @SerializedName("username")
        private String mUsername;

        public String getUsername() {
            return mUsername;
        }
    }
}

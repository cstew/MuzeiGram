package com.cstewart.android.muzeigram.data.instagram;

import com.google.gson.annotations.SerializedName;

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

    @SerializedName("user")
    private InstagramUser mUser;

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
        if (mUser == null) {
            return "";
        }

        return mUser.getUsername();
    }

    public boolean isImageType() {
        return "image".equals(mMediaType);
    }

    public InstagramUser getUser() {
        return mUser;
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

        public String getText() {
            return mText;
        }
    }
}

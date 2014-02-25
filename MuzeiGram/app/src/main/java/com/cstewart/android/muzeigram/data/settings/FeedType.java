package com.cstewart.android.muzeigram.data.settings;

import com.cstewart.android.muzeigram.R;

public enum FeedType {
    CUSTOM(4, R.string.feed_type_custom),
    POPULAR(0, R.string.feed_type_popular),
    FEED(1, R.string.feed_type_feed),
    LIKED(2, R.string.feed_type_liked),
    PERSONAL(3, R.string.feed_type_personal);

    private int mIndex;
    private int mTitleResId;

    private FeedType(int index, int titleResId) {
        mIndex = index;
        mTitleResId = titleResId;
    }

    public int getIndex() {
        return mIndex;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public static FeedType from(int index) {
        for (FeedType feedType : FeedType.values()) {
            if (feedType.getIndex() == index) {
                return feedType;
            }
        }

        return FeedType.CUSTOM;
    }
}

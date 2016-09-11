package com.cstewart.android.muzeigram.data.settings;

import com.cstewart.android.muzeigram.R;

public enum FeedType {
//    Instagram has all but killed their API. Just personal photos for now.
    PERSONAL(3, R.string.feed_type_personal),;

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

        return FeedType.PERSONAL;
    }
}

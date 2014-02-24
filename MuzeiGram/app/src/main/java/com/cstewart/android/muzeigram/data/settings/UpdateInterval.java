package com.cstewart.android.muzeigram.data.settings;

import com.cstewart.android.muzeigram.R;

public enum UpdateInterval {

    TEN_SECONDS (10000, R.id.action_rotate_interval_10s),
    MINUTE(60000, R.id.action_rotate_interval_1m),
    FIVE_MINUTES(300000, R.id.action_rotate_interval_5m),
    FIFTEEN_MINUTES(900000, R.id.action_rotate_interval_15m),
    THIRTY_MINUTES(1800000, R.id.action_rotate_interval_30m),
    HOUR(3600000, R.id.action_rotate_interval_1h),
    THREE_HOURS(10800000, R.id.action_rotate_interval_3h),
    SIX_HOURS(21600000, R.id.action_rotate_interval_6h),
    TWELVE_HOURS(43200000, R.id.action_rotate_interval_12h),
    EVERY_DAY(86400000, R.id.action_rotate_interval_1d);

    private int mMilliseconds;
    private int mMenuResId;

    private UpdateInterval(int milliseconds, int menuResId) {
        mMilliseconds = milliseconds;
        mMenuResId = menuResId;
    }

    public int getMilliseconds() {
        return mMilliseconds;
    }

    public int getMenuResId() {
        return mMenuResId;
    }

    public static UpdateInterval fromMenuResId(int menuResId) {
        for (UpdateInterval interval : UpdateInterval.values()) {
            if (interval.getMenuResId() == menuResId) {
                return interval;
            }
        }

        return null;
    }

    public static UpdateInterval from(int milliseconds) {
        for (UpdateInterval interval : UpdateInterval.values()) {
            if (interval.getMilliseconds() == milliseconds) {
                return interval;
            }
        }

        return SIX_HOURS;
    }
}

package com.cstewart.android.muzeigram.data;

import com.cstewart.android.muzeigram.R;

public enum UpdateInterval {

    TEN_SECONDS (10000, R.string.update_interval_ten_seconds),
    MINUTE(60000, R.string.update_interval_minute),
    HOUR(3600000, R.string.update_interval_hour),
    THREE_HOURS(10800000, R.string.update_interval_three_hours),
    SIX_HOURS(21600000, R.string.update_interval_six_hours),
    TWELVE_HOURS(43200000, R.string.update_interval_twelve_hours),
    EVERY_DAY(86400000, R.string.update_interval_day);

    private int mMilliseconds;
    private int mNameResId;

    private UpdateInterval(int milliseconds, int nameResId) {
        mMilliseconds = milliseconds;
        mNameResId = nameResId;
    }

    public int getMilliseconds() {
        return mMilliseconds;
    }

    public int getNameResId() {
        return mNameResId;
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

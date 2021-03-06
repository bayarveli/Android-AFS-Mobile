package com.example.afs_mobile;

import java.util.List;

public class TabDataStructure {
    public boolean mIsActivated;
    public int mTaskTimeHour;
    public int mTaskTimeMinute;
    public int mPondTime;
    public int mFeedingTime;

    public TabDataStructure() {
        mIsActivated = false;
        mTaskTimeHour = 0;
        mTaskTimeMinute = 0;
        mPondTime = 30;
        mFeedingTime = 30;
    }
}

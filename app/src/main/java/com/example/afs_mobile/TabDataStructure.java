package com.example.afs_mobile;

import java.util.List;

//typedef struct
//{
//    uint16_t year;
//    uint8_t month;
//    uint8_t day;
//    uint8_t dayOfWeek;
//    uint8_t hours;
//    uint8_t minutes;
//    uint8_t seconds;
//} DateTime;

public class TabDataStructure {
    public boolean mIsActivated;
    public int mTaskTimeYear;
    public int mTaskTimeMonth;
    public int mTaskTimeDay;
    public int mTaskTimeDayOfWeek;
    public int mTaskTimeHour;
    public int mTaskTimeMinute;
    public int mTaskTimeSecond;
    public int mPondCount;
    public int[] mPondTime;
    public int[] mFeedingTime;

    public TabDataStructure() {
        mIsActivated = false;
        mTaskTimeDay = 0;
        mTaskTimeYear = 0;
        mTaskTimeDayOfWeek = 0;
        mTaskTimeMonth = 0;
        mTaskTimeHour = 0;
        mTaskTimeMinute = 0;
        mTaskTimeSecond = 0;
        mPondCount = 0;
        mPondTime = new int[16];
        mFeedingTime = new int[16];
    }
}

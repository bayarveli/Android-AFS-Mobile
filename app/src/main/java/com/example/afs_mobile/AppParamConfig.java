package com.example.afs_mobile;

public class AppParamConfig {
    public static final int SYSTEM_POND_COUNT = 10;
    public static final int SYSTEM_POND_CONFIG_SIZE_IN_EEPROM = 58;

    // Copied from message.h
    public static final int SYSTEM_MSD_ID_START_CONFIG = 0x01;
    public static final int SYSTEM_MSD_ID_END_CONFIG = 0x02;
    public static final int SYSTEM_MSG_ID_TASK_INFO	= 0x03;
    public static final int SYSTEM_MSG_ID_SET_TIME = 0x04;
    public static final int SYSTEM_MSG_ID_GET_TIME = 0x05;
    public static final int SYSTEM_MSG_ID_GET_STATE = 0x06;
    public static final int SYSTEM_MSG_ID_DRIVE_FRWD = 0x07;
    public static final int SYSTEM_MSG_ID_DRIVE_BCWD = 0x08;
    public static final int SYSTEM_MSG_ID_DRIVE_STOP = 0x09;
    public static final int SYSTEM_MSG_ID_DRIVE_SET_SPEED = 0x0A;
    public static final int SYSTEM_MSG_ID_FEED_START = 0x0B;
    public static final int SYSTEM_MSG_ID_FEED_STOP = 0x0C;
    public static final int SYSTEM_MSG_ID_FEED_SET_SPEED = 0x0D;
    public static final int SYSTEM_MSG_ID_ABORT_TASK = 0x99;
}

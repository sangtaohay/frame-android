package com.sangtaohay.memestudio.contract;

import android.provider.BaseColumns;

public class MemeContract {
    public static final String DB_NAME = "com.sangtaohay.memestudio.db";
    public static final int DB_VERSION = 9;

    public class MemeEntry implements BaseColumns {
        public static final String TABLE = "meme";

        public static final String COL_TITLE = "title";
        public static final String COL_STATUS = "status";
        public static final String COL_POSITION = "position";
        public static final String COL_TEXT_TOP = "textTop";
        public static final String COL_TEXT_BOTTOM = "textBottom";
        public static final String COL_TEXT_BOTTOM_LEFT = "textBottomLeft";
        public static final String COL_TEXT_BOTTOM_RIGHT = "textBottomRight";
        public static final String COL_IMAGE = "image";
        public static final String COL_CODE = "code";
        public static final String COL_THUMB = "thumb";
        public static final String COL_CREATEDAT = "createdAt";
        public static final String COL_BACKGROUNDCOLOR = "backgroundColor";
        public static final String COL_WIDTH = "imageWidth";
        public static final String COL_HEIGHT = "imageHeight";
        public static final String COL_TYPE = "type"; //1: static background, 2: animated background

    }
}

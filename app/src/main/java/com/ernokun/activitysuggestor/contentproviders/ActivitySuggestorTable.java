package com.ernokun.activitysuggestor.contentproviders;

import android.provider.BaseColumns;

public class ActivitySuggestorTable {

    private ActivitySuggestorTable() {}

    public static final class MyColumns implements BaseColumns {
        public static final String TABLE_NAME = "activities_table";
        public static final String COLUMN_ACTIVITY_CATEGORY = "category";
        public static final String COLUMN_ACTIVITY_DESCRIPTION = "description";
    }
}

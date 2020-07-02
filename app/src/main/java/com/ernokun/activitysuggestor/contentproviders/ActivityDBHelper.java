package com.ernokun.activitysuggestor.contentproviders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ernokun.activitysuggestor.contentproviders.ActivitySuggestorTable.*;
import com.ernokun.activitysuggestor.utils.ActivitySuggestion;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ActivityDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "activities.db";
    public static final int DATABASE_VERSION = 1;

    public ActivityDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String QUERY = "CREATE TABLE " + MyColumns.TABLE_NAME +
                " (" +
                MyColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MyColumns.COLUMN_ACTIVITY_CATEGORY + " TEXT NOT NULL, " +
                MyColumns.COLUMN_ACTIVITY_DESCRIPTION + " TEXT NOT NULL" +
                ")";

        db.execSQL(QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MyColumns.TABLE_NAME);
        onCreate(db);
    }

    public void addToDatabase(ActivitySuggestion suggestion) {
        if (checkIfAlreadyInDatabase(suggestion))
            return;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(MyColumns.COLUMN_ACTIVITY_CATEGORY, suggestion.getCategory());
        cv.put(MyColumns.COLUMN_ACTIVITY_DESCRIPTION, suggestion.getDescription());

        db.insert(MyColumns.TABLE_NAME, null, cv);

        db.close();
    }

    private boolean checkIfAlreadyInDatabase(ActivitySuggestion suggestion) {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT * FROM " + MyColumns.TABLE_NAME + " WHERE " +
                MyColumns.COLUMN_ACTIVITY_CATEGORY + " = ?" + " AND " +
                MyColumns.COLUMN_ACTIVITY_DESCRIPTION + " = ?";

        Cursor cursor = db.rawQuery(QUERY, new String[]{suggestion.getCategory(), suggestion.getDescription()});

        return cursor.moveToFirst();
    }

    public void deleteFromDatabase(ActivitySuggestion suggestion) {
        SQLiteDatabase db = this.getWritableDatabase();

        final String QUERY = "DELETE FROM " + MyColumns.TABLE_NAME + " WHERE "
                + MyColumns.COLUMN_ACTIVITY_CATEGORY + " = ?" + " AND "
                + MyColumns.COLUMN_ACTIVITY_DESCRIPTION + " = ?";

        db.execSQL(QUERY, new String[]{suggestion.getCategory(), suggestion.getDescription()});

        db.close();
    }

    public ArrayList<ActivitySuggestion> readDatabase(SQLiteDatabase db) {
        ArrayList<ActivitySuggestion> suggestions = new ArrayList<>();

        String query = "SELECT * FROM " + MyColumns.TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String category = cursor.getString(1);
            String description = cursor.getString(2);

            suggestions.add(new ActivitySuggestion(category, description));
        }

        return suggestions;
    }
}

package com.ernokun.activitysuggestor.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.ernokun.activitysuggestor.broadcastreceivers.MyDatabaseReceiver;
import com.ernokun.activitysuggestor.contentproviders.ActivityDBHelper;
import com.ernokun.activitysuggestor.utils.ActivitySuggestion;

public class MyDatabaseService extends JobIntentService {
    private ActivityDBHelper dbHelper;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, MyDatabaseService.class, 123, intent);
    }

    public void saveActivityDB(ActivitySuggestion suggestion) {
        dbHelper.addToDatabase(suggestion);
    }

    public void deleteActivityDB(ActivitySuggestion suggestion) {
        dbHelper.deleteFromDatabase(suggestion);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dbHelper = new ActivityDBHelper(this);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        ActivitySuggestion suggestion = intent.getParcelableExtra(MyDatabaseReceiver.MY_EXTRA_ACTIVITY_SUGGESTION);

        if (MyDatabaseReceiver.MY_ACTION_SAVE_SUGGESTION.equals(intent.getAction()))
            saveActivityDB(suggestion);

        else if (MyDatabaseReceiver.MY_ACTION_DELETE_SUGGESTION.equals(intent.getAction()))
            deleteActivityDB(suggestion);

        if (isStopped())
            return;
    }

    @Override
    public boolean onStopCurrentWork() {
        return super.onStopCurrentWork();
    }
}

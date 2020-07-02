package com.ernokun.activitysuggestor.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ernokun.activitysuggestor.services.MyDatabaseService;

public class MyDatabaseReceiver extends BroadcastReceiver {

    // BrowseActivity sends this:
    public static final String MY_ACTION_SAVE_SUGGESTION = "com.ernokun.activitysuggestor.save_suggestion";

    // ViewSavedActivity sends this:
    public static final String MY_ACTION_DELETE_SUGGESTION = "com.ernokun.activitysuggestor.delete_suggestion";

    // Intent extras:
    public static final String MY_EXTRA_ACTIVITY_SUGGESTION = "com.ernokun.activitysuggestor.extra_suggestion";


    @Override
    public void onReceive(Context context, Intent intent) {
        intent.setClass(context, MyDatabaseService.class);

        MyDatabaseService.enqueueWork(context.getApplicationContext(), intent);
    }

}

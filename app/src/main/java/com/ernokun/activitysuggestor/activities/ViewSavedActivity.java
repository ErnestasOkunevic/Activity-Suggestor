package com.ernokun.activitysuggestor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.ernokun.activitysuggestor.R;
import com.ernokun.activitysuggestor.broadcastreceivers.MyDatabaseReceiver;
import com.ernokun.activitysuggestor.contentproviders.ActivityDBHelper;
import com.ernokun.activitysuggestor.utils.ActivitySuggestion;
import com.ernokun.activitysuggestor.utils.MyRecyclerViewAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ViewSavedActivity extends AppCompatActivity {

    private RecyclerView myRecyclerView;

    private ArrayList<ActivitySuggestion> suggestions;

    private MyRecyclerViewAdapter myRecyclerViewAdapter;

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallBack =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            ActivitySuggestion suggestion = suggestions.get(viewHolder.getAdapterPosition());
            int position = viewHolder.getAdapterPosition();

            suggestions.remove(position);

            myRecyclerViewAdapter.notifyItemRemoved(position);

            showMessage("Deleting...");

            deleteSuggestionDB(suggestion);
        }
    };

    // Sends broadcast to run service which will
    // delete the suggestion from the database
    private void deleteSuggestionDB(ActivitySuggestion suggestion) {
        Intent intent = new Intent();

        intent.setClass(this, MyDatabaseReceiver.class);
        intent.setAction(MyDatabaseReceiver.MY_ACTION_DELETE_SUGGESTION);
        intent.putExtra(MyDatabaseReceiver.MY_EXTRA_ACTIVITY_SUGGESTION, suggestion);

        sendBroadcast(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_saved_acitivity);

        myRecyclerView = findViewById(R.id.saved_activities_recycler_view_id);

        suggestions = new ArrayList<>();

        new ReadDatabaseTask(this).execute();

        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, suggestions);
        myRecyclerView.setLayoutManager(new LinearLayoutManager((this)));

        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(myRecyclerView);
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Returns all of the data inside of the database.
    private static class ReadDatabaseTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<ViewSavedActivity> weakReference;

        private ArrayList<ActivitySuggestion> async_suggestions;

        private ActivityDBHelper helper;


        public ReadDatabaseTask(ViewSavedActivity viewSavedActivity) {
            this.weakReference = new WeakReference<ViewSavedActivity>(viewSavedActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ViewSavedActivity activity = weakReference.get();

            if (activity == null || activity.isFinishing())
                return;

            helper = new ActivityDBHelper(activity.getApplicationContext());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SQLiteDatabase db = helper.getReadableDatabase();

            async_suggestions = helper.readDatabase(db);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ViewSavedActivity activity = weakReference.get();

            if (activity == null || activity.isFinishing())
                return;

            activity.suggestions.addAll(async_suggestions);

            activity.myRecyclerViewAdapter.notifyDataSetChanged();

        }
    }

}
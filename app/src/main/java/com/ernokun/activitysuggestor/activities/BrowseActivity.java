package com.ernokun.activitysuggestor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.ernokun.activitysuggestor.R;
import com.ernokun.activitysuggestor.broadcastreceivers.MyDatabaseReceiver;
import com.ernokun.activitysuggestor.utils.ActivitySuggestion;
import com.ernokun.activitysuggestor.utils.MyBrowseRecyclerViewAdapter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BrowseActivity extends AppCompatActivity {

    private RecyclerView browseRecyclerView;

    private MyBrowseRecyclerViewAdapter browseRecyclerViewAdapter;


    private ArrayList<ActivitySuggestion> suggestions;

    // The current activity suggestion.
    private ActivitySuggestion currentSuggestion;

    // The current activity picture id.
    private int currentPicture;


    // Used to make sure that only one GET request is active.
    private boolean isCurrentlyRunning = false;

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            ActivitySuggestion suggestion = suggestions.get(viewHolder.getAdapterPosition());

            removeCurrentActivity();

            if (direction == ItemTouchHelper.LEFT) // NEXT
                showMessage("Loading next...");

            else if (direction == ItemTouchHelper.RIGHT) { // SAVE
                showMessage("Saving ...");

                saveData();
            }

            fetchData();
        }
    };

    public void removeCurrentActivity() {
        suggestions.remove(0);

        browseRecyclerViewAdapter.notifyItemRemoved(0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        suggestions = new ArrayList<>();

        setupRecyclerView();

        String currentCategory = MainActivity.getRandomCategory();
        String currentDescription = "loading...";

        currentPicture = MainActivity.getCategoryPictureId(currentCategory);

        currentSuggestion = new ActivitySuggestion(currentCategory, currentDescription);

    }

    @Override
    protected void onStart() {
        super.onStart();

        fetchData();

        showMessage("Loading suggestions...");
    }


    private void setupRecyclerView() {
        browseRecyclerView = findViewById(R.id.browse_recycler_view_id);
        browseRecyclerViewAdapter = new MyBrowseRecyclerViewAdapter(this, suggestions);

        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(browseRecyclerView);

        browseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        browseRecyclerView.setAdapter(browseRecyclerViewAdapter);
    }

    public void fetchData() {
        if (!isCurrentlyRunning) {
            String category = MainActivity.getRandomCategory();

            FetchDataFromWebTask initialData = new FetchDataFromWebTask(this, category);

            initialData.execute();
        }
    }

    public void saveData() {
        Intent intent = new Intent();

        intent.setClass(this, MyDatabaseReceiver.class);
        intent.setAction(MyDatabaseReceiver.MY_ACTION_SAVE_SUGGESTION);
        intent.putExtra(MyDatabaseReceiver.MY_EXTRA_ACTIVITY_SUGGESTION, currentSuggestion);

        sendBroadcast(intent);
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private static class FetchDataFromWebTask extends AsyncTask<Void, Void, Void> {
        // Needed to make sure that this AsyncTask
        // doesn't cause memory leaks.
        private WeakReference<BrowseActivity> activityWeakReference;

        // The category we are looking for.
        private String fetchedCategory;

        // New activity suggestion
        private String fetchedActivity;

        public FetchDataFromWebTask(BrowseActivity activity, String category) {
            activityWeakReference = new WeakReference<BrowseActivity>(activity);

            fetchedCategory = category;
            fetchedActivity = "couldn't fetch data...";
        }

        @Override
        protected void onPreExecute() {
            BrowseActivity activity = activityWeakReference.get();
            activity.isCurrentlyRunning = true;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url_string = MainActivity.SELECT_TYPE_WEBSITE_URL + fetchedCategory.toLowerCase();

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(url_string);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line = reader.readLine()) != null)
                    buffer.append(line + "\n");

                String jsonDataString = buffer.toString();
                JSONObject jsonData = new JSONObject(jsonDataString);

                fetchedActivity = jsonData.getString("activity") + ".";

                connection.disconnect();
                reader.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            BrowseActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing())
                return;

            activity.currentSuggestion.setDescription(fetchedActivity);
            activity.currentSuggestion.setCategory(fetchedCategory);

            activity.suggestions.add(activity.currentSuggestion);
            activity.browseRecyclerViewAdapter.notifyItemInserted(0);

            activity.isCurrentlyRunning = false;
        }
    }
}
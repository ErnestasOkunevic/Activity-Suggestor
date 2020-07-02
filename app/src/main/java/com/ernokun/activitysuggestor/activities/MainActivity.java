package com.ernokun.activitysuggestor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ernokun.activitysuggestor.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // For the API requests:
    public static final String SELECT_TYPE_WEBSITE_URL = "https://www.boredapi.com/api/activity?type=";
    //   The GET request looks like this:                 http://www.boredapi.com/api/activity?type=recreational


    // Used by OnPressActivity:
    public static final String EXTRA_CATEGORY_NAME = "CATEGORY_NAME";
    public static final String EXTRA_CATEGORY_PICTURE = "CATEGORY_PICTURE";
    public static final String EXTRA_CATEGORY_DESCRIPTION = "CATEGORY_DESCRIPTION";

    // Array with category names:
    public static String[] categories = new String[] {
            "Education", "Recreational", "Social", "Diy", "Charity",
            "Cooking", "Relaxation", "Music", "Busywork"};

    // Gets a picture id associated with the given category;
    public static int getCategoryPictureId(String categoryName) {
        int pictureId = R.drawable.ic_launcher_background;

        if ("recreational".equalsIgnoreCase(categoryName))
            pictureId = R.drawable.recreational;

        else if ("education".equalsIgnoreCase(categoryName))
            pictureId = R.drawable.education;

        else if ("social".equalsIgnoreCase(categoryName))
            pictureId = R.drawable.social;

        else if ("diy".equalsIgnoreCase(categoryName))
            pictureId = R.drawable.diy;

        else if ("charity".equalsIgnoreCase(categoryName))
            pictureId = R.drawable.charity;

        else if ("cooking".equalsIgnoreCase(categoryName))
            pictureId = R.drawable.cooking;

        else if ("relaxation".equalsIgnoreCase(categoryName))
            pictureId = R.drawable.relaxation;

        else if ("music".equalsIgnoreCase(categoryName))
            pictureId = R.drawable.music;

        else if ("busywork".equalsIgnoreCase(categoryName))
            pictureId = R.drawable.busywork;

        return pictureId;
    }

    // Gives a random category:
    public static String getRandomCategory() {
        return categories[new Random().nextInt(categories.length)];
    }

    private void enableButtons(boolean shouldBeEnabled) {
        browseButton.setEnabled(shouldBeEnabled);
        viewSavedButton.setEnabled(shouldBeEnabled);
    }


    private Button browseButton, viewSavedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browseButton = findViewById(R.id.browse_button_id);
        viewSavedButton = findViewById(R.id.view_saved_button_id);

        browseButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowseActivity();
                enableButtons(false);
            }
        });

        viewSavedButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewSavedActivity();
                enableButtons(false);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        enableButtons(true);
    }

    private void openBrowseActivity() {
        Intent intent = new Intent(this, BrowseActivity.class);
        startActivity(intent);
    }

    private void openViewSavedActivity() {
        Intent intent = new Intent(this, ViewSavedActivity.class);
        startActivity(intent);
    }
}
package com.ernokun.activitysuggestor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.ernokun.activitysuggestor.R;
import com.ernokun.activitysuggestor.activities.MainActivity;

import java.util.Objects;

public class OnPressActivity extends AppCompatActivity {

    // The category name.
    private TextView categoryName;


    // The category specific photo.
    private ImageView categoryPicture;


    // The actual activity suggestion text.
    private TextView activityDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_press);

        categoryName = findViewById(R.id.category_name_id);
        categoryPicture = findViewById(R.id.category_picture_id);
        activityDescription = findViewById(R.id.category_activity_description_id);

        categoryName.setText(getIntent().getStringExtra(MainActivity.EXTRA_CATEGORY_NAME));

        categoryPicture.setImageResource(Integer.parseInt(
                Objects.requireNonNull(
                        getIntent().getStringExtra(MainActivity.EXTRA_CATEGORY_PICTURE))));

        activityDescription.setText(getIntent().getStringExtra(MainActivity.EXTRA_CATEGORY_DESCRIPTION));

        activityDescription.setMovementMethod(new ScrollingMovementMethod());
    }
}
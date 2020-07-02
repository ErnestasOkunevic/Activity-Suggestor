package com.ernokun.activitysuggestor.utils;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ernokun.activitysuggestor.R;
import com.ernokun.activitysuggestor.activities.MainActivity;
import com.ernokun.activitysuggestor.activities.OnPressActivity;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<ActivitySuggestion> suggestions;

    private Context context;

    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 600;


    public MyRecyclerViewAdapter(Context context, ArrayList<ActivitySuggestion> suggestions) {
        this.suggestions = suggestions;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_recycler_view_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        String description_text = suggestions.get(position).getDescription();

        String[] words = description_text.split(" ");

        if (words.length > 3)
            description_text = words[0] + " " + words[1] + " " + words[2] + "...";

        holder.description.setText(description_text);

        final int imageId = MainActivity.getCategoryPictureId(suggestions.get(position).getCategory());
        holder.image.setImageResource(imageId);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long now = System.currentTimeMillis();

                if (now - mLastClickTime < CLICK_TIME_INTERVAL)
                    return;

                mLastClickTime = now;

                Intent intent = new Intent(context, OnPressActivity.class);

                intent.putExtra(MainActivity.EXTRA_CATEGORY_NAME, suggestions.get(holder.getAdapterPosition()).getCategory());
                intent.putExtra(MainActivity.EXTRA_CATEGORY_PICTURE, Integer.toString(imageId));
                intent.putExtra(MainActivity.EXTRA_CATEGORY_DESCRIPTION, suggestions.get(holder.getAdapterPosition()).getDescription());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView description;
        LinearLayout layout;
        
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.my_row_picture_id);
            description = itemView.findViewById(R.id.my_row_text_id);
            layout = itemView.findViewById(R.id.my_row_layout_id);
        }
    }
}

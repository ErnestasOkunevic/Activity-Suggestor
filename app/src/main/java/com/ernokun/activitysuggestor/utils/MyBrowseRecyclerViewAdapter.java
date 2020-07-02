package com.ernokun.activitysuggestor.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ernokun.activitysuggestor.R;
import com.ernokun.activitysuggestor.activities.MainActivity;

import java.util.ArrayList;

public class MyBrowseRecyclerViewAdapter extends RecyclerView.Adapter<MyBrowseRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<ActivitySuggestion> suggestions;

    private Context context;

    public MyBrowseRecyclerViewAdapter(Context context, ArrayList<ActivitySuggestion> suggestions) {
        this.context = context;
        this.suggestions = suggestions;
    }

    @NonNull
    @Override
    public MyBrowseRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse_recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBrowseRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.description.setText(suggestions.get(position).getDescription());

        final int imageId = MainActivity.getCategoryPictureId(suggestions.get(position).getCategory());
        holder.image.setImageResource(imageId);
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.browse_recycler_row_picture_id);
            description = itemView.findViewById(R.id.browse_recycler_row_description_id);
        }
    }
}

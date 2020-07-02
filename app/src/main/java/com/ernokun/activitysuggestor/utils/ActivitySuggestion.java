package com.ernokun.activitysuggestor.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class ActivitySuggestion implements Parcelable {
    private String category;
    private String description;

    public ActivitySuggestion(String category, String description) {
        this.category = category;
        this.description = description;
    }

    protected ActivitySuggestion(Parcel in) {
        category = in.readString();
        description = in.readString();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ActivitySuggestion{" +
                "category='" + category + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(description);
    }

    public static final Creator<ActivitySuggestion> CREATOR = new Creator<ActivitySuggestion>() {
        @Override
        public ActivitySuggestion createFromParcel(Parcel in) {
            return new ActivitySuggestion(in);
        }

        @Override
        public ActivitySuggestion[] newArray(int size) {
            return new ActivitySuggestion[size];
        }
    };
}

package com.hust.zl.daily.gson;

import com.google.gson.annotations.SerializedName;

public class TopStory {
    public String type;

    @SerializedName("id")
    public String storyId;

    public String title;

    public String image;

    @SerializedName("ga_prefix")
    public String googleAnalyticsPrefix;
}

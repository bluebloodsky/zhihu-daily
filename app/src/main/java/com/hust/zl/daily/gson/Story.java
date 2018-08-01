package com.hust.zl.daily.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Story {
    public String type;

    @SerializedName("id")
    public String storyId;

    public String title;

    public List<String> images;

    public boolean multipic;
    @SerializedName("ga_prefix")
    public String googleAnalyticsPrefix;

}

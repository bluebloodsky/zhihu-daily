package com.hust.zl.daily.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SectionStory {
    public String date;

    @SerializedName("display_date")
    public String displayDate;
    @SerializedName("id")
    public String storyId;
    public String title;
    public List<String> images;
}

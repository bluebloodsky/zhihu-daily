package com.hust.zl.daily.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SectionNews {

    @SerializedName("stories")
    public List<SectionStory> sectionStories;
    public String timestamp;
    public String name;
}

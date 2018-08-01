package com.hust.zl.daily.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LatestNews {
    public String date;

    @SerializedName("stories")
    public List<Story> storyList;

    @SerializedName("top_stories")
    public List<TopStory> topStoryList;
}

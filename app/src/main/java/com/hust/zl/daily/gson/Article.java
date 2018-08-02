package com.hust.zl.daily.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Article {
    public String body;

    @SerializedName("image_source")
    public String imageSource;
    public String title;
    public String image;

    @SerializedName("share_url")
    public String shareUrl;
    public String type;
    public String id;
    public List<String> images;

    public List<String> js;

    @SerializedName("ga_prefix")
    public String googleAnalyticsPrefix;

    public List<String> css;
}

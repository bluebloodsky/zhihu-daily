package com.hust.zl.daily.util;

import com.google.gson.Gson;
import com.hust.zl.daily.gson.Article;
import com.hust.zl.daily.gson.SectionNews;
import com.hust.zl.daily.gson.StoryNews;

public class Utility {
    public static StoryNews handleLatestNews(String response) {
        try {
            return new Gson().fromJson(response, StoryNews.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Article handleArticle(String response) {
        try {
            return new Gson().fromJson(response, Article.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SectionNews handleSectionNews(String response) {
        try {
            return new Gson().fromJson(response, SectionNews.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

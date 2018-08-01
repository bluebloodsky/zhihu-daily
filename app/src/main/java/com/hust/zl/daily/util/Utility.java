package com.hust.zl.daily.util;

import com.google.gson.Gson;
import com.hust.zl.daily.gson.LatestNews;

import org.json.JSONArray;
import org.json.JSONObject;

public class Utility {
    public static LatestNews handleLatestNews(String response) {
        try {
            return new Gson().fromJson(response, LatestNews.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

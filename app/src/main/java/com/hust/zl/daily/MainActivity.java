package com.hust.zl.daily;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hust.zl.daily.gson.LatestNews;
import com.hust.zl.daily.gson.Story;
import com.hust.zl.daily.gson.TopStory;
import com.hust.zl.daily.util.HttpUtil;
import com.hust.zl.daily.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends Activity {

    private FrameLayout topStoryLayout;
    private LinearLayout storyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topStoryLayout = (FrameLayout) findViewById(R.id.top_story_layout);
        storyLayout = (LinearLayout) findViewById(R.id.story_layout);
        storyLayout.setVisibility(View.INVISIBLE);
        requestStories();
    }

    private void requestStories() {
        String storyUrl = "http://news-at.zhihu.com/api/4/news/latest";
        HttpUtil.sendOkHttpRequest(storyUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final LatestNews latestNews = Utility.handleLatestNews(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (latestNews != null) {
                            showStories(latestNews);
                        }
                    }
                });
            }
        });
    }

    private void showStories(LatestNews latestNews) {
        for (TopStory topStory : latestNews.topStoryList) {
            View view = LayoutInflater.from(this).inflate(R.layout.top_story_item, topStoryLayout, false);
            TextView storyTitle = (TextView) view.findViewById(R.id.story_title);
            ImageView stroyImg = (ImageView) view.findViewById(R.id.story_img);
            storyTitle.setText(topStory.title);
            Glide.with(MainActivity.this).load(topStory.image).into(stroyImg);
        }
        for (Story story : latestNews.storyList) {
            View view = LayoutInflater.from(this).inflate(R.layout.story_item, topStoryLayout, false);
            TextView storyTitle = (TextView) view.findViewById(R.id.story_title);
            ImageView stroyImg = (ImageView) view.findViewById(R.id.story_img);
            storyTitle.setText(story.title);
            Glide.with(MainActivity.this).load(story.images.get(0)).into(stroyImg);

        }
    }
}

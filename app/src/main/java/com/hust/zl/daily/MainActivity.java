package com.hust.zl.daily;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hust.zl.daily.gson.Article;
import com.hust.zl.daily.gson.LatestNews;
import com.hust.zl.daily.gson.Story;
import com.hust.zl.daily.gson.TopStory;
import com.hust.zl.daily.other.LoadMoreScrollListener;
import com.hust.zl.daily.util.CommonUtil;
import com.hust.zl.daily.util.HttpUtil;
import com.hust.zl.daily.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends Activity {

    private RecyclerView recyclerViewStories;
    private SwipeRefreshLayout refreshLayout;
    private String currentDate;

    private List<Story> storyList;
    StoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewStories = (RecyclerView) findViewById(R.id.rv_stories);

        recyclerViewStories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        storyList = new ArrayList<>(0);
        adapter = new StoryAdapter(storyList);
        recyclerViewStories.setAdapter(adapter);

        currentDate = CommonUtil.getToday();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        requestStories();

        recyclerViewStories.addOnScrollListener(new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                requestbeforeStories();
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestStories();
            }
        });
    }

    private void requestbeforeStories() {
        String url = "https://news-at.zhihu.com/api/4/news/before/" + currentDate;

        HttpUtil.sendOkHttpRequest(url, new Callback() {
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
                            currentDate = latestNews.date;
                            storyList.addAll(latestNews.storyList);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    private void requestStories() {
        String storyUrl = "https://news-at.zhihu.com/api/4/news/latest";
        HttpUtil.sendOkHttpRequest(storyUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                        refreshLayout.setRefreshing(false);
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
                            storyList.clear();
                            storyList.addAll(latestNews.storyList);
                            adapter.notifyDataSetChanged();
                        }
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }
}

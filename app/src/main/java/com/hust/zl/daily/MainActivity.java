package com.hust.zl.daily;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hust.zl.daily.gson.Article;
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

    private ViewPager topStoryViewPager;
    private LinearLayout storyLayout;

    private LinearLayout llPoint;
    private int currentTopStoryPage;

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topStoryViewPager = (ViewPager) findViewById(R.id.top_story_view_pager);
        llPoint = (LinearLayout) findViewById(R.id.ll_point);

        storyLayout = (LinearLayout) findViewById(R.id.story_layout);
        storyLayout.setVisibility(View.INVISIBLE);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        requestStories();

        topStoryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                llPoint.getChildAt(currentTopStoryPage).setEnabled(false);
                llPoint.getChildAt(i).setEnabled(true);
                currentTopStoryPage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestStories();
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
                            showStories(latestNews);
                        }
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void showStories(LatestNews latestNews) {
        llPoint.removeAllViews();
        TopStoryPagerAdapter topStoryPagerAdapter = new TopStoryPagerAdapter(latestNews.topStoryList);
        topStoryViewPager.setAdapter(topStoryPagerAdapter);
        for (int i = 0; i < latestNews.topStoryList.size(); i++) {
            View pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.selector_bg_point);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            if (i != 0) {
                layoutParams.leftMargin = 15;
            }
            pointView.setEnabled(false);
            llPoint.addView(pointView, layoutParams);
        }


        llPoint.getChildAt(0).setEnabled(true);
        topStoryViewPager.setCurrentItem(0);
        currentTopStoryPage = 0;

        for (final Story story : latestNews.storyList) {
            View view = LayoutInflater.from(this).inflate(R.layout.story_item, storyLayout, false);
            TextView storyTitle = (TextView) view.findViewById(R.id.story_title);
            ImageView stroyImg = (ImageView) view.findViewById(R.id.story_img);
            storyTitle.setText(story.title);
            Glide.with(MainActivity.this).load(story.images.get(0)).into(stroyImg);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                    intent.putExtra("story_id", story.storyId);
                    startActivity(intent);
                }
            });
            storyLayout.addView(view);
        }
        storyLayout.setVisibility(View.VISIBLE);
    }
}

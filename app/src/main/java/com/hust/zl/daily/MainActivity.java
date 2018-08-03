package com.hust.zl.daily;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hust.zl.daily.gson.StoryNews;
import com.hust.zl.daily.gson.Story;
import com.hust.zl.daily.gson.TopStory;
import com.hust.zl.daily.other.HeaderAndFooterWrapper;
import com.hust.zl.daily.other.LoadMoreScrollListener;
import com.hust.zl.daily.other.RefreshLayout;
import com.hust.zl.daily.util.CommonUtil;
import com.hust.zl.daily.util.HttpUtil;
import com.hust.zl.daily.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends Activity {

    private RecyclerView recyclerViewStories;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout llPoint;
    private ViewPager topStoryViewPager;

    private String currentDate;

    //向下刷新只会更新今日热点文章
    private int newestStroyCount = 0;

    private int currentTopStoryPage = 0;

    private List<TopStory> topStoryList;
    private TopStoryPagerAdapter topStoryPagerAdapter;
    //包括日期和文章
    private List<Object> storyList;
    HeaderAndFooterWrapper adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewStories = (RecyclerView) findViewById(R.id.rv_stories);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewStories.setLayoutManager(linearLayoutManager);

        View headerView = LayoutInflater.from(this).inflate(R.layout.top_story_layout, recyclerViewStories, false);

        llPoint = (LinearLayout) headerView.findViewById(R.id.ll_point);
        topStoryViewPager = (ViewPager) headerView.findViewById(R.id.top_story_view_pager);
        topStoryList = new ArrayList<>(0);

        topStoryPagerAdapter = new TopStoryPagerAdapter(topStoryList);
        topStoryViewPager.setAdapter(topStoryPagerAdapter);

        storyList = new ArrayList<>(0);
        StoryAdapter storyAdapter = new StoryAdapter(storyList);
        adapter = new HeaderAndFooterWrapper(storyAdapter);

        adapter.addHeaderView(headerView);
        recyclerViewStories.setAdapter(adapter);

        currentDate = CommonUtil.getToday();
        refreshLayout = (RefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        requestNewestStories();

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
        recyclerViewStories.addOnScrollListener(new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                requestbeforeStories(this);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestNewestStories();
            }
        });
    }

    private void requestbeforeStories(final LoadMoreScrollListener listener) {
        String url = "https://news-at.zhihu.com/api/4/news/before/" + currentDate;

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                        if (listener != null) {
                            listener.LoadFinish();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final StoryNews storyNews = Utility.handleLatestNews(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (storyNews != null) {
                            currentDate = storyNews.date;
                            storyList.add(CommonUtil.getShowDate(currentDate));
                            storyList.addAll(storyNews.storyList);
                            adapter.notifyDataSetChanged();
                        }
                        if (listener != null) {
                            listener.LoadFinish();
                        }
                    }
                });
            }
        });
    }

    private void requestNewestStories() {
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
                final StoryNews storyNews = Utility.handleLatestNews(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (storyNews != null) {
                            showTopStories(storyNews.topStoryList);
                            if (newestStroyCount != 0) {
                                for (int i = 0; i < newestStroyCount + 1; i++) {
                                    storyList.remove(0);
                                }
                            }
                            storyList.addAll(0, storyNews.storyList);
                            storyList.add(0, "今日热闻");
                            newestStroyCount = storyNews.storyList.size();
                            adapter.notifyDataSetChanged();
                        }
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void showTopStories(List<TopStory> stories) {
        llPoint.removeAllViews();
        for (int i = 0; i < stories.size(); i++) {
            View pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.selector_bg_point);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            if (i != 0) {
                layoutParams.leftMargin = 15;
            }
            pointView.setEnabled(false);
            llPoint.addView(pointView, layoutParams);
        }

        topStoryList.clear();
        topStoryList.addAll(stories);

        topStoryPagerAdapter.notifyDataSetChanged();

        llPoint.getChildAt(0).setEnabled(true);
        topStoryViewPager.setCurrentItem(0);
        currentTopStoryPage = 0;
    }
}

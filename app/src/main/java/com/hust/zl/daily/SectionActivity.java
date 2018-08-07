package com.hust.zl.daily;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hust.zl.daily.gson.SectionNews;
import com.hust.zl.daily.other.LoadMoreScrollListener;
import com.hust.zl.daily.util.HttpUtil;
import com.hust.zl.daily.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SectionActivity extends Activity {

    private Button backButton;
    private TextView titleTextView;
    private RecyclerView recyclerViewStories;
    private List<Object> sectionStoryList;
    private StoryAdapter adapter;
    private String sectionId;
    private String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        backButton = (Button) findViewById(R.id.back_button);
        recyclerViewStories = (RecyclerView) findViewById(R.id.rv_stories);

        titleTextView = (TextView) findViewById(R.id.section_name);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewStories.setLayoutManager(linearLayoutManager);

        sectionStoryList = new ArrayList<>(0);
        adapter = new StoryAdapter(sectionStoryList);

        recyclerViewStories.setAdapter(adapter);
        recyclerViewStories.addOnScrollListener(new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                requestbeforeSectionStories(this);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sectionId = getIntent().getStringExtra("section_id");
        requestSectionStories();
    }

    private void requestbeforeSectionStories(final LoadMoreScrollListener listener) {
        String storyUrl = "https://news-at.zhihu.com/api/4/section/" + sectionId + "/before/" + timeStamp;
        HttpUtil.sendOkHttpRequest(storyUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SectionActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                        if (listener != null) {
                            listener.LoadFinish();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final SectionNews storyNews = Utility.handleSectionNews(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (storyNews != null) {
                            timeStamp = storyNews.timestamp;
                            sectionStoryList.addAll(storyNews.sectionStories);
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

    private void requestSectionStories() {

        String storyUrl = "https://news-at.zhihu.com/api/4/section/" + sectionId;
        HttpUtil.sendOkHttpRequest(storyUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SectionActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final SectionNews storyNews = Utility.handleSectionNews(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (storyNews != null) {
                            titleTextView.setText(storyNews.name);
                            timeStamp = storyNews.timestamp;
                            sectionStoryList.addAll(storyNews.sectionStories);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

}

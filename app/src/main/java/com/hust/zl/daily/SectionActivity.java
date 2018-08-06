package com.hust.zl.daily;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.hust.zl.daily.gson.SectionNews;
import com.hust.zl.daily.gson.SectionStory;
import com.hust.zl.daily.other.LoadMoreScrollListener;
import com.hust.zl.daily.other.RefreshLayout;
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
    private RecyclerView rv_stories;
    List<Object> sectionStoryList;
    StoryAdapter adapter;
    private String sectionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        backButton = (Button) findViewById(R.id.back_button);
        rv_stories = (RecyclerView) findViewById(R.id.rv_stories);

        sectionStoryList = new ArrayList<>(0);
        StoryAdapter adapter = new StoryAdapter(sectionStoryList);

        rv_stories.setAdapter(adapter);
        rv_stories.addOnScrollListener(new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                requestbeforeSectionStories(this);
            }
        });

        sectionId = getIntent().getStringExtra("section_id");
//        requestSectionStories();
    }

    private void requestbeforeSectionStories(final LoadMoreScrollListener listener) {

    }

    private void requestSectionStories(){

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
                            sectionStoryList.addAll(storyNews.sectionStories);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

}

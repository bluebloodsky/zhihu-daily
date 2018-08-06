package com.hust.zl.daily;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hust.zl.daily.gson.Article;
import com.hust.zl.daily.util.HtmlUtil;
import com.hust.zl.daily.util.HttpUtil;
import com.hust.zl.daily.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ArticleActivity extends Activity {

    public static final String TAG = ArticleActivity.class.getName();
    WebView webView;

    Button backButton;
    ImageView articleImg;
    TextView articleTitle;
    TextView imgSource;

    LinearLayout section_layout;
    ImageView section_img;
    TextView section_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        webView = (WebView) findViewById(R.id.web_view);
        backButton = (Button) findViewById(R.id.back_button);
        articleImg = (ImageView) findViewById(R.id.story_img);
        articleTitle = (TextView) findViewById(R.id.story_title);
        imgSource = (TextView) findViewById(R.id.img_source);
        section_layout = (LinearLayout) findViewById(R.id.section_layout);
        section_img = (ImageView) findViewById(R.id.section_img);
        section_name = (TextView) findViewById(R.id.section_name);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String storyId = getIntent().getStringExtra("story_id");
        String articleString = preferences.getString("article_" + storyId, null);

        if (articleString != null) {
            Article article = Utility.handleArticle(articleString);
            showArticle(article);
        } else {
            requestArticle(storyId);
        }
    }

    private void showArticle(final Article article) {
        articleTitle.setText(article.title);
        imgSource.setText("图片：" + article.imageSource);
        Glide.with(this).load(article.image).into(articleImg);
        String data = HtmlUtil.createHtmlData(article.body, article.css, article.js);
        webView.loadData(data, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
        if (article.section != null) {
            Glide.with(this).load(article.section.thumbnail).into(section_img);
            section_name.setText("本文来自：" + article.section.sectionName + "·合集");
            section_layout.setVisibility(View.VISIBLE);
            section_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),SectionActivity.class);
                    intent.putExtra("section_id",article.section.sectionId);
                    view.getContext().startActivity(intent);

                }
            });

        }
    }

    private void requestArticle(String storyId) {
        String url = "http://news-at.zhihu.com/api/4/news/" + storyId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String articleString = response.body().string();
                final Article article = Utility.handleArticle(articleString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (article != null) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ArticleActivity.this).edit();
                            editor.putString("article_" + article.id, articleString);
                            editor.apply();
                            showArticle(article);
                        }
                    }
                });
            }
        });
    }
}

package com.hust.zl.daily;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hust.zl.daily.gson.TopStory;

import java.util.List;

public class TopStoryPagerAdapter extends PagerAdapter {

    private List<TopStory> topStoryList;

    public TopStoryPagerAdapter(List<TopStory> objects) {
        topStoryList = objects;

    }

    @Override
    public int getCount() {
        return topStoryList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, int position) {
        final TopStory story = topStoryList.get(position);
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.top_story_item , container,false);
        TextView storyTitle = (TextView) view.findViewById(R.id.story_title);
        ImageView stroyImg = (ImageView) view.findViewById(R.id.story_img);

        storyTitle.setText(story.title);
        Glide.with(container.getContext()).load(story.image).into(stroyImg);
        container.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context,ArticleActivity.class);
                intent.putExtra("story_id" , story.storyId);
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }


}

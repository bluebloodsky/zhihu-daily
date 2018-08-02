package com.hust.zl.daily;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hust.zl.daily.gson.Story;
import com.hust.zl.daily.gson.TopStory;

import java.util.List;

public class TopStoryAdapter extends RecyclerView.Adapter<TopStoryAdapter.MyViewHoder> {

    static class MyViewHoder extends RecyclerView.ViewHolder {
        TextView storyTitle;
        ImageView stroyImg;

        public MyViewHoder(View view) {
            super(view);
            storyTitle = (TextView) view.findViewById(R.id.story_title);
            stroyImg = (ImageView) view.findViewById(R.id.story_img);
        }
    }

    private List<TopStory> storyList;

    private Context context;

    public TopStoryAdapter(List<TopStory> objects) {
        storyList = objects;
    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_story_item, viewGroup, false);
        MyViewHoder hoder = new MyViewHoder(view);
        return hoder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder myViewHoder, int i) {
        TopStory story = storyList.get(i);
        myViewHoder.storyTitle.setText(story.title);
        Glide.with(context).load(story.image).into(myViewHoder.stroyImg);
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }
}

package com.hust.zl.daily;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hust.zl.daily.gson.Story;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.MyViewHolder> {

    static class MyViewHolder extends RecyclerView.ViewHolder {
        View storyView;
        TextView storyTitle;
        ImageView stroyImg;

        public MyViewHolder(View view) {
            super(view);
            storyView = view;
            storyTitle = (TextView) view.findViewById(R.id.story_title);
            stroyImg = (ImageView) view.findViewById(R.id.story_img);
        }
    }

    private List<Story> storyList;

    private Context context;

    public StoryAdapter(List<Story> objects) {
        storyList = objects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.story_item, viewGroup, false);
        final MyViewHolder holder = new MyViewHolder(view);
        holder.storyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Story story = storyList.get(position);
                Context context = v.getContext();
                Intent intent = new Intent(context,ArticleActivity.class);
                intent.putExtra("story_id" , story.storyId);
                context.startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHoder, int i) {
        Story story = storyList.get(i);
        myViewHoder.storyTitle.setText(story.title);
        Glide.with(context).load(story.images.get(0)).into(myViewHoder.stroyImg);
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }
}

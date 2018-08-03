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

public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int STORY_VIEW = 0;
    private static final int DATE_VIEW = 1;

    static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public DateViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.tv_date);
        }
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {
        View storyView;
        TextView storyTitle;
        ImageView stroyImg;

        public StoryViewHolder(View view) {
            super(view);
            storyView = view;
            storyTitle = (TextView) view.findViewById(R.id.story_title);
            stroyImg = (ImageView) view.findViewById(R.id.story_img);
        }
    }

    private List<Object> storyList;

    private Context context;

    public StoryAdapter(List<Object> objects) {
        storyList = objects;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        if (viewType == STORY_VIEW) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.story_item, viewGroup, false);
            final StoryViewHolder holder = new StoryViewHolder(view);
            return holder;
        } else if (viewType == DATE_VIEW) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.date_item, viewGroup, false);
            final DateViewHolder holder = new DateViewHolder(view);
            return holder;
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        Object obj = storyList.get(position);
        if (viewHolder instanceof StoryViewHolder) {
            final Story story = (Story) obj;
            final StoryViewHolder storyViewHolder = (StoryViewHolder) viewHolder;
            storyViewHolder.storyTitle.setText(story.title);
            Glide.with(context).load(story.images.get(0)).into(storyViewHolder.stroyImg);

            storyViewHolder.storyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storyViewHolder.storyTitle.setTextColor(v.getContext().getResources().getColor(R.color.readColor));
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ArticleActivity.class);
                    intent.putExtra("story_id", story.storyId);
                    context.startActivity(intent);

                }
            });
        } else if (viewHolder instanceof DateViewHolder) {
            String date = (String) obj;
            DateViewHolder dateViewHolder = (DateViewHolder) viewHolder;
            dateViewHolder.textView.setText(date);
        }
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object object = storyList.get(position);
        if (object instanceof Story) {
            return STORY_VIEW;
        } else if (object instanceof String) {
            return DATE_VIEW;
        }
        return super.getItemViewType(position);
    }
}

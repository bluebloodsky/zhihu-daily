package com.hust.zl.daily.other;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

public abstract class LoadMoreScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        if(newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.getAdapter().getItemCount() > 1
                && lastVisibleItemPosition + 1 == recyclerView.getAdapter().getItemCount()){
            onLoadMore();
        }
    }

    public abstract void onLoadMore();
}

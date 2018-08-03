package com.hust.zl.daily.other;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

public abstract class LoadMoreScrollListener extends RecyclerView.OnScrollListener {

    //防止重复加载
    private boolean onLoadingState = false;

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        if (!onLoadingState && newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.getAdapter().getItemCount() > 1
                && lastVisibleItemPosition + 2 >= recyclerView.getAdapter().getItemCount()) {
            onLoadMore();
            onLoadingState = true;
        }
    }

    public void LoadFinish() {
        onLoadingState = false;
    }

    public abstract void onLoadMore();
}

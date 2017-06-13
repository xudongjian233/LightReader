package com.xudongjian.lightreader.ui.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xudongjian on 17/4/12.
 * RecyclerView的分隔线
 */

public class RVItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 1, 0, 1);
    }
}

package com.caco3.mvk.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * {@see http://blog.sqisland.com/2014/12/recyclerview-autofit-grid.html}
 */
public class AutoFitRecyclerView extends RecyclerView {
  private GridLayoutManager gridLayoutManager;
  private int columnWidth = -1;

  public AutoFitRecyclerView(Context context) {
    super(context);
    init(context, null);
  }

  public AutoFitRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public AutoFitRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs){
    if (attrs != null) {
      int[] attrsArray = {
              android.R.attr.columnWidth
      };
      TypedArray array = null;
      try {
        array = context.obtainStyledAttributes(attrs, attrsArray);
        columnWidth = array.getDimensionPixelSize(0, -1);
      } finally {
        if (array != null) {
          array.recycle();
        }
      }
    }

    gridLayoutManager = new GridLayoutManager(getContext(), 1);
    setLayoutManager(gridLayoutManager);
  }

  @Override
  protected void onMeasure(int widthSpec, int heightSpec) {
    super.onMeasure(widthSpec, heightSpec);
    if (columnWidth > 0) {
      int spanCount = Math.max(1, getMeasuredWidth() / columnWidth);
      gridLayoutManager.setSpanCount(spanCount);
    }
  }
}
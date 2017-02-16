package com.caco3.mvk.ui.fab;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;


public class ScrollAwareFabBehavior extends FloatingActionButton.Behavior {
  /** http://stackoverflow.com/questions/41153619/floating-action-button-not-visible-on-scrolling-after-updating-google-support **/
  private static final FloatingActionButton.OnVisibilityChangedListener
          onVisibilityChangedListener = new FloatingActionButton.OnVisibilityChangedListener() {
    @Override
    public void onHidden(FloatingActionButton fab) {
      super.onShown(fab);
      fab.setVisibility(View.INVISIBLE);
    }
  };


  public ScrollAwareFabBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                     FloatingActionButton child, View directTargetChild,
                                     View target, int nestedScrollAxes) {
    return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
            || super.onStartNestedScroll(coordinatorLayout, child,
            directTargetChild, target, nestedScrollAxes);
  }

  @Override
  public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                             View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                             int dyUnconsumed) {
    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
            dyUnconsumed);
    if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
      child.hide(onVisibilityChangedListener);
    } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
      child.show();
    }
  }
}

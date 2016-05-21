package com.example.teachcooking.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class SlidingView extends ViewGroup{
	private Scroller mScroller;
	public static final int SCROLL_DURATION = 700;
	public SlidingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context){
		mScroller = new Scroller(context);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).layout(l, t, getWidth()/3, b);
		}
	}
	
	@Override
	public void computeScroll() {
		if (!mScroller.isFinished()) {
			if (mScroller.computeScrollOffset()) {
				int oldX = getScrollX();
				int oldY = getScrollY();
				int x = mScroller.getCurrX();
				int y = mScroller.getCurrY();
				if (oldX != x || oldY != y) {
					scrollTo(x, y);
				}
				invalidate();
			} else {
				clearChildrenCache();
			}
		} else {
			clearChildrenCache();
		}
	}
	public void smoothScrollTo(int desX,int desY){
		int oldScrollX = getScrollX();
		int oldScrollY = getScrollY();
		int dxX = desX - oldScrollX;
		int dxY = desY - oldScrollY;
		mScroller.startScroll(oldScrollX, oldScrollY, dxX, dxY, SCROLL_DURATION);
		invalidate();
	}

	private void clearChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(false);
		}
	}
}

package com.example.teachcooking.view;

import java.util.ArrayList;
import java.util.List;

import com.example.teachcooking.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;


public class CustomHorizontalTabView extends HorizontalScrollView{
	private List<CheckedTextView> mTextView = new ArrayList<CheckedTextView>();
	private LayoutInflater mInflater;
	private LinearLayout.LayoutParams params;
	private CheckedTextView mCurTextView;
	private int mTabWidth;
	private int mGapWidth = 10;
//	private int mTabHeight;
	public CustomHorizontalTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
//		mTabHeight = Integer.parseInt(getResources().getString(R.string.tab_height));
		mTabWidth = Integer.parseInt(getResources().getString(R.string.tab_width));
		init(context);
	}

	private void init(Context context) {
		mScroller = new Scroller(context);
		mInflater = LayoutInflater.from(context);
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		addView(layout);
	}

	/**
	 * 增加文本的方法
	 * @param texts
	 */
	public void addViews(List<String> texts){
		for (String text : texts) {
			CheckedTextView tv = (CheckedTextView) mInflater.inflate(R.layout.item_linearlayout, null);
			tv.setText(text);
			params.rightMargin = mGapWidth;
			layout.addView(tv,params);
			mTextView.add(tv);
			tv.setOnClickListener(onClickListener);
		}
		mTextView.get(0).setChecked(true);
		mCurTextView = mTextView.get(0);
	}

	private OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if(v == mCurTextView){
				return;
			}
			for (int i = 0; i <mTextView.size(); i++) {
				if(v == mTextView.get(i)){
					if(onTabSelectedListener !=null){
						onTabSelectedListener.onTabSelected(i);
					}
					mCurTextView.toggle();
					mTextView.get(i).toggle();
					mCurTextView = mTextView.get(i);
					break;
				}
			}
		}
	};



	/**
	 * 滑动的方法
	 * @param position
	 */
	public void select(int position){
		mCurTextView.toggle();
		mTextView.get(position).toggle();
		mCurTextView = mTextView.get(position);
		if(position > 1){
			smoothScroll((position-1)*(mTabWidth+mGapWidth), 0);
		}else{
			smoothScroll(0,0);
		}
	}
	private Scroller mScroller;
	public static final int SCROLL_DURATION = 500;
	@Override
	public void computeScroll() {
		super.computeScroll();
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
			}
		}
	}
	public void smoothScroll(int desX,int desY){
		int oldScrollX = getScrollX();
		int oldScrollY = getScrollY();
		int dxX = desX - oldScrollX;
		int dxY = desY - oldScrollY;
		mScroller.startScroll(oldScrollX, oldScrollY, dxX, dxY, SCROLL_DURATION);
		invalidate();
	}

	public interface OnTabSelectedListener{
		void onTabSelected(int position);
	}

	private OnTabSelectedListener onTabSelectedListener;
	private LinearLayout layout;
	public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
		this.onTabSelectedListener = onTabSelectedListener;
	}
}

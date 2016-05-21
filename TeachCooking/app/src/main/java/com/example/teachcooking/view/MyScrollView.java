package com.example.teachcooking.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * �������û���������ScrollView
 * @author Li
 *
 */
public class MyScrollView extends ScrollView{

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(onScrollChangeListener != null){
			onScrollChangeListener.onScrollChange(l, t);
		}
	}
	
	public interface OnScrollChangeListener{
		void onScrollChange(int l, int t);
	}
	private OnScrollChangeListener onScrollChangeListener;
	public void setOnScrollChangeListener(
			OnScrollChangeListener onScrollChangeListener) {
		this.onScrollChangeListener = onScrollChangeListener;
	}
	
}

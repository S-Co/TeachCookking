package com.example.teachcooking.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CookCategoryGridView extends GridView{
	public static final int HORIZONTAL_SPACING = 15;
	public static final int VERTICAL_SPACING = 15;
	public static final int PADDING = 15;
	public static final int NUM_COLUMNS = 4;

	public CookCategoryGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		setHorizontalSpacing(HORIZONTAL_SPACING);
		setVerticalSpacing(VERTICAL_SPACING);
		setPadding(PADDING, 0, PADDING, 0);
		setNumColumns(NUM_COLUMNS);
	}

	
}

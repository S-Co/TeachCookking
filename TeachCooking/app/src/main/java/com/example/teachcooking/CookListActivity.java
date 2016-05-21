package com.example.teachcooking;

import java.util.ArrayList;
import java.util.List;

import com.example.teachcooking.base.FragmentTabActivity;
import com.example.teachcooking.fragment.FragmentAll;
import com.example.teachcooking.fragment.FragmentCollect;
import com.example.teachcooking.fragment.FragmentNearby;
import com.example.teachcooking.view.SlidingView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class CookListActivity extends FragmentTabActivity{

	private String title;
	private View mTagView;
	private int mTagWidth;
	private SlidingView mSlidingView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_cook_list);
		title = getIntent().getStringExtra("title");
		super.onCreate(savedInstanceState);
		
		initTabs();
		
	}
	
	private void initTabs() {
		List<Fragment> fragments = new ArrayList<Fragment>();
		FragmentAll fragmentAll = new FragmentAll();
		//对第一个Fragment 设置内容
		fragmentAll.setCid(getIntent().getStringExtra("cid"));
		fragmentAll.setSearch_key(getIntent().getStringExtra("search_key"));
		//初始化三个Fragment的界面
		fragments.add(fragmentAll);
		fragments.add(new FragmentNearby());
		fragments.add(new FragmentCollect());
		//对三个tag标签 初始化并设置监听
		int[] checkedTvs = {R.id.tab_all,R.id.tab_nearby,R.id.tab_collect};
		init(R.id.content, fragments, checkedTvs);
		//设置红线移动的监听
		setOnTabChangeListener(onTabChangeListener);
		
		initTagLineView(fragments.size());
	}
	
	private void initTagLineView(int tabCount) {
		//初始化红线View
		mSlidingView = (SlidingView) findViewById(R.id.slidingView);
		mTagView = findViewById(R.id.tag_view);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		//红线宽度
		mTagWidth = dm.widthPixels/tabCount;
		LayoutParams params = (LayoutParams) mTagView.getLayoutParams();
		params.width = mTagWidth;
		mTagView.setLayoutParams(params);
	}
	
	private OnTabChangeListener onTabChangeListener = new OnTabChangeListener() {
		
		@Override
		public void onTabChange(int currentSelected) {
			moveTagView(currentSelected);
		}
	};
	
	/**
	 * 移动横线
	 * @param index
	 */
	private void moveTagView(int index){
		mSlidingView.smoothScrollTo(-index*mTagWidth, 0);
	}
	
	@Override
	public void initTitle() {
		initBack();
		initTitleText(title);
	}
}

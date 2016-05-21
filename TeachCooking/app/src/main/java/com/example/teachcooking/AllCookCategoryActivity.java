package com.example.teachcooking;

import java.util.ArrayList;
import java.util.List;

import com.example.teachcooking.adpter.CookCategoryListAdapter;
import com.example.teachcooking.base.TitleActivity;
import com.example.teachcooking.entity.ResponseClassifys;
import com.example.teachcooking.entity.ResponseClassifys.Classify;
import com.example.teachcooking.view.CustomHorizontalTabView;
import com.example.teachcooking.view.CustomHorizontalTabView.OnTabSelectedListener;

import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

/**
 * 菜谱分类
 * 
 * @author Li
 * 
 */
public class AllCookCategoryActivity extends TitleActivity {
	private CustomHorizontalTabView myView; //title 上的自定义View
	private ListView mListView;
	private CookCategoryListAdapter mListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_all_cook_category);
		super.onCreate(savedInstanceState);
		myView = (CustomHorizontalTabView) findViewById(R.id.myView);

		mListView = (ListView) findViewById(R.id.listView);
		// 获得菜单信息对象
		ResponseClassifys responseClassifys = (ResponseClassifys) getIntent()
				.getSerializableExtra("data");
		List<String> mTexts = new ArrayList<String>();
		//给title内容赋值
		for (Classify classify : responseClassifys.getResult()) {
			mTexts.add(classify.getName());
		}
		//给myView上的View 赋值
		myView.addViews(mTexts);
		//设置滚动监听（给CustomHorizontalTabView 中的onTabSelectedListener 赋值）
		myView.setOnTabSelectedListener(onTabSelectedListener);
		mListAdapter = new CookCategoryListAdapter(
				responseClassifys.getResult(), this);
		mListView.setAdapter(mListAdapter);
		//监听listView滑动时，改变title上的View
		mListView.setOnScrollListener(onScrollListener);
	}

	OnScrollListener onScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (mFirstVisibleItem != firstVisibleItem) {//判断重复的不改变CustomHorizontalTabView
				mFirstVisibleItem = firstVisibleItem;
				myView.select(firstVisibleItem);//改变myView的Item选项
			}
		}
	};
	private int mFirstVisibleItem = 0;
	OnTabSelectedListener onTabSelectedListener = new OnTabSelectedListener() {

		@Override
		public void onTabSelected(int position) {
			mListView.setSelection(position);//改变listView上显示的位置
		}
	};
	//初始化title 内容
	@Override
	public void initTitle() {
		initBack();
		initTitleText("菜谱分类");
	}

}

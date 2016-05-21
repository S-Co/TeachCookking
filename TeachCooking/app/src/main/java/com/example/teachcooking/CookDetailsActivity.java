package com.example.teachcooking;

import com.example.teachcooking.adpter.CookStepListAdapter;
import com.example.teachcooking.base.TitleActivity;
import com.example.teachcooking.db.DbHelper;
import com.example.teachcooking.entity.CookBook;
import com.example.teachcooking.util.ImageLoader;
import com.example.teachcooking.view.MyScrollView;
import com.example.teachcooking.view.MyScrollView.OnScrollChangeListener;
import com.example.teachcooking.view.ViewUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CookDetailsActivity extends TitleActivity{
	
	private CookBook cookBook;
	private MyScrollView mScrollView;
	private ListView mListView;
	private CookStepListAdapter mListAdapter;
	private ImageLoader mImgLoader;
	private DbHelper mDbHelper;
	
	private View layout1,layout2;
	private CheckedTextView mCollectTv1,mCollectTv2;
	private TextView mShareTv1,mShareTv2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_cook_details);
		cookBook = (CookBook) getIntent().getSerializableExtra("cookBook");
		mDbHelper = new DbHelper(this);
		super.onCreate(savedInstanceState);

		mImgLoader = new ImageLoader();
		initViews();
		
		saveCookDetail();
	}

	private void saveCookDetail() {
		if(!mDbHelper.exitCookBookInNearBy(cookBook)){
			mDbHelper.insertCookDetailToNearBy(cookBook);
		}
	}

	private void initViews() {
		mScrollView = (MyScrollView) findViewById(R.id.scrollView);
		mScrollView.setOnScrollChangeListener(onScrollChangeListener);
		layout1 = findViewById(R.id.layout);
		layout2 = findViewById(R.id.layout2);
		mCollectTv1 = (CheckedTextView) findViewById(R.id.collect_tv_1);
		mCollectTv2 = (CheckedTextView) findViewById(R.id.collect_tv_2);
		
		if(mDbHelper.exitCookBookInCollect(cookBook)){
			mCollectTv1.setChecked(true);
			mCollectTv2.setChecked(true);
			mCollectTv1.setText("已收藏");
			mCollectTv2.setText("已收藏");
		}else{
			mCollectTv1.setChecked(false);
			mCollectTv2.setChecked(false);
			mCollectTv1.setText("收藏");
			mCollectTv2.setText("收藏");
		}
		mShareTv1 = (TextView) findViewById(R.id.share_tv);
		mShareTv2 = (TextView) findViewById(R.id.share_tv2);
		mCollectTv1.setOnClickListener(onClickListener);
		mCollectTv2.setOnClickListener(onClickListener);
		mShareTv1.setOnClickListener(onClickListener);
		mShareTv2.setOnClickListener(onClickListener);
		initListView();
	}
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v == mCollectTv1 || v == mCollectTv2){
				if(mCollectTv1.isChecked()){
					mCollectTv1.setText("收藏");
					mCollectTv2.setText("收藏");
					mDbHelper.deleteCookDetailFromCollect(cookBook);
					((Myapplication) getApplication()).toast("收藏");
				}else{
					mCollectTv1.setText("取消收藏");
					mCollectTv2.setText("取消收藏");
					mDbHelper.insertCookDetailToCollect(cookBook);
					((Myapplication) getApplication()).toast("收藏成功");
				}
				mCollectTv1.toggle();
				mCollectTv2.toggle();
			}else if(v == mShareTv1 || v == mShareTv2){
				
				
			}
		}
	};
	
	private OnScrollChangeListener onScrollChangeListener = new OnScrollChangeListener() {
		
		@Override
		public void onScrollChange(int l, int t) {
			if(t >= layout1.getTop()){
				layout2.setVisibility(View.VISIBLE);
			}else{
				layout2.setVisibility(View.GONE);
			}
		}
	};
	
	private void initHeaderView(){
		
		ImageView img = (ImageView)findViewById(R.id.header_img);//菜title大图
		img.setTag(cookBook.getAlbums().get(0));
		mImgLoader.displayImg(cookBook.getAlbums().get(0), img);//设置大图
		TextView nameTv = (TextView)findViewById(R.id.name);
		TextView nameTv2 = (TextView) findViewById(R.id.name2);
		nameTv.setText(cookBook.getTitle());
		nameTv2.setText(cookBook.getTitle());
		LinearLayout layout = (LinearLayout)findViewById(R.id.materials_layout);//放食材
		TextView introTv = (TextView)findViewById(R.id.text_intro);//简介
		introTv.setText(cookBook.getImtro());
		//食材明细
		String ingredients = cookBook.getIngredients();//主食材
		String burden = cookBook.getBurden();//辅助食材
		String materialsStr = ingredients+ ";" + burden;
		String[] split = materialsStr.split(";");
		//每行放两项  算用多少行
		int lines = (split.length % 2) == 0 ? split.length/2 : split.length/2+1;
		for (int i = 0; i < lines; i++) {
			View view = LayoutInflater.from(this).inflate(R.layout.item_material, null);
			layout.addView(view);
			String[] texts = split[i*2].split(",");
			TextView tv1 = (TextView) view.findViewById(R.id.text1);
			tv1.setText(texts[0]);
			TextView tv2 = (TextView) view.findViewById(R.id.text2);
			tv2.setText(texts[1]);

			if(i == lines-1 && split.length % 2 != 0){
				
				continue;
			}
			texts = split[i*2+1].split(",");
			TextView tv3 = (TextView) view.findViewById(R.id.text3);
			tv3.setText(texts[0]);
			TextView tv4 = (TextView) view.findViewById(R.id.text4);
			tv4.setText(texts[1]);
			texts = null;
		}
	}
	
	
	private void initListView() {
		initHeaderView();
		mListView = (ListView) findViewById(R.id.listView);
		mListAdapter = new CookStepListAdapter(this, cookBook.getSteps());
		mListView.setAdapter(mListAdapter);
		mListView.setOnScrollListener(onScrollListener);
		ViewUtil.setListViewHeightBasedOnChildren(mListView);
		mScrollView.scrollTo(0, 0);
		mListView.setOnItemClickListener(onItemClickListener);
		
	}
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Myapplication application = (Myapplication) getApplication();
			application.cookSteps =  cookBook.getSteps();
			application.position = position;
			Intent intent = new Intent(CookDetailsActivity.this, CookStepPagerActivity.class);
			startActivity(intent);
		}
	};
	
	OnScrollListener onScrollListener = new OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			System.out.println(mListView.getScrollY());
		}
	};
	@Override
	public void initTitle() {
		initTitleText("菜谱详情");
		initBack();
	}


}

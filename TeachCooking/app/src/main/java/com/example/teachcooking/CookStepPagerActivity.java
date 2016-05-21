package com.example.teachcooking;

import java.util.ArrayList;
import java.util.List;

import com.example.teachcooking.base.TitleActivity;
import com.example.teachcooking.entity.CookStep;
import com.example.teachcooking.util.ImageLoader;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class CookStepPagerActivity extends TitleActivity{
	
	private ViewPager mViewPager;
	private List<CookStep> mCookSteps;
	private int mPosition;
	private List<View> views;
	private ImageLoader mImageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_step_pager);
		super.onCreate(savedInstanceState);
		mImageLoader = new ImageLoader();
		initDatas();
		initViews();
	}
	private void initViews() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		views = new ArrayList<View>();
		LayoutInflater mInflater = LayoutInflater.from(this);
		for (int i = 0; i < mCookSteps.size(); i++) {
			View view = mInflater.inflate(R.layout.item_cook_step_pager, null);
			ViewHolder holder = new ViewHolder();
			holder.img = (ImageView) view.findViewById(R.id.img);
			holder.stepNum = (TextView) view.findViewById(R.id.step_num);
			holder.text = (TextView) view.findViewById(R.id.text);
			view.setTag(holder);
			views.add(view);
		}
		mViewPager.setAdapter(new CookStepPagerAdapter(views));
		mViewPager.setCurrentItem(mPosition);
	}
	
	
	private void initDatas() {
		Myapplication app = (Myapplication) getApplication();
		mCookSteps = app.cookSteps;
		mPosition = app.position;
	}
	@Override
	public void initTitle() {
		initBack();
		initTitleText("大图");
	}
	class ViewHolder{
		ImageView img;
		TextView stepNum;
		TextView text;
	}
	class CookStepPagerAdapter extends PagerAdapter{
		private List<View> views;
		
		public CookStepPagerAdapter(List<View> views) {
			super();
			this.views = views;
		}

		@Override
		public int getCount() {
			return views.size();
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			int pos = position;
			View view = views.get(pos);
			ViewHolder holder = (ViewHolder) view.getTag();
			holder.text.setText(mCookSteps.get(pos).getStep());
			holder.stepNum.setText(pos+1+"/"+views.size());
			mImageLoader.displayImg(mCookSteps.get(pos).getImg(), holder.img);
			container.addView(view);
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			int pos = position;
			View view = views.get(pos);
			container.removeView(view);
		}
	}
	
}

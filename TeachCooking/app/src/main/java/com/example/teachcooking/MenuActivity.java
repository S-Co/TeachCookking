package com.example.teachcooking;

import java.util.ArrayList;

import com.example.teachcooking.adpter.ViewPageAdpter;
import com.example.teachcooking.db.ProvinceHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;

public class MenuActivity extends FragmentActivity implements OnClickListener,
		OnPageChangeListener {
	private RadioButton basket;
	private RadioButton around;
	private RadioButton teach;
	private RadioButton mine;
	private MineActivity mineActivity = new MineActivity();
	private TeachActivity teachActivity = new TeachActivity();
	private BasketActivity basketActivity = new BasketActivity();
	private AroundActivity aroundActivity = new AroundActivity();
	private ViewPager viewPager;
	private ViewPageAdpter adpter;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private ArrayList<RadioButton> textViews = new ArrayList<RadioButton>();
	private ProvinceHelper helper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu);
		// 存入城市和省份数据
		helper = new ProvinceHelper();
		helper.savaDate(this);
		basket = (RadioButton) findViewById(R.id.menu_basket);
		around = (RadioButton) findViewById(R.id.menu_around);
		teach = (RadioButton) findViewById(R.id.menu_teach);
		mine = (RadioButton) findViewById(R.id.menu_mine);
		viewPager = (ViewPager) findViewById(R.id.menu_viewpager);

		basket.setOnClickListener(this);
		teach.setOnClickListener(this);
		around.setOnClickListener(this);
		mine.setOnClickListener(this);

		textViews.add(teach);
		textViews.add(basket);
		textViews.add(around);
		textViews.add(mine);
		fragments.add(teachActivity);
		fragments.add(basketActivity);
		fragments.add(aroundActivity);
		fragments.add(mineActivity);

		adpter = new ViewPageAdpter(getSupportFragmentManager(), fragments);
		teach.setChecked(true);
		viewPager.setAdapter(adpter);
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_teach:
			viewPager.setCurrentItem(0);
			break;
		case R.id.menu_basket:
			viewPager.setCurrentItem(1);
			break;
		case R.id.menu_around:
			viewPager.setCurrentItem(2);
			break;
		case R.id.menu_mine:
			viewPager.setCurrentItem(3);
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		textViews.get(arg0).setChecked(true);
	}
}

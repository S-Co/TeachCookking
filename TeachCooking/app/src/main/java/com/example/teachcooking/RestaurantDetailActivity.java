package com.example.teachcooking;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.baidu.mapapi.utils.route.RouteParaOption.EBusStrategyType;
import com.example.teachcooking.entity.Restaurant;
import com.example.teachcooking.util.ImageLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantDetailActivity extends Activity implements
		OnClickListener {
	private Restaurant restaurant;
	// 餐厅图片
	private ImageView restaurant_detail_iv;
	// 餐厅名字
	private TextView restaurant_detail_rName;
	// 人均消费
	private TextView restaurant_detail_price;
	// 饭店位置
	private TextView restaurant_detail_navigation;
	// 饭店标签
	private TextView restaurant_detail_tags;
	// 口味
	private TextView product_rating;
	// 环境
	private TextView enviroment_rating;
	// 服务
	private TextView service_rating;
	// 地址
	private TextView restaurant_address;
	// 电话
	private TextView restaurant_phone;
	// 网友推荐
	private TextView recommended_dishes;
	private ImageLoader imageLoader;
	// 星级评分
	private ImageView restaurant_detail_star;
	private double longgitude;
	private double latitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 透明状态栏
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		setContentView(R.layout.activity_restaurant_detail);
		init();
	}

	private void init() {
		restaurant = (Restaurant) getIntent()
				.getSerializableExtra("restaurant");
		longgitude = getIntent().getDoubleExtra("longgitude", 0);
		latitude = getIntent().getDoubleExtra("latitude", 0);
		restaurant_detail_iv = (ImageView) findViewById(R.id.restaurant_detail_iv);
		restaurant_detail_rName = (TextView) findViewById(R.id.restaurant_detail_rName);
		restaurant_detail_price = (TextView) findViewById(R.id.restaurant_detail_price);
		restaurant_detail_navigation = (TextView) findViewById(R.id.restaurant_detail_navigation);
		restaurant_detail_tags = (TextView) findViewById(R.id.restaurant_detail_tags);
		product_rating = (TextView) findViewById(R.id.product_rating);
		enviroment_rating = (TextView) findViewById(R.id.enviroment_rating);
		service_rating = (TextView) findViewById(R.id.service_rating);
		restaurant_address = (TextView) findViewById(R.id.restaurant_address);
		restaurant_phone = (TextView) findViewById(R.id.restaurant_phone);
		recommended_dishes = (TextView) findViewById(R.id.recommended_dishes);
		restaurant_detail_star = (ImageView) findViewById(R.id.restaurant_detail_star);
		restaurant_phone.setOnClickListener(this);
		restaurant_address.setOnClickListener(this);
		imageLoader = new ImageLoader();

		imageLoader.displayImg(restaurant.getPhotos(), restaurant_detail_iv);
		restaurant_detail_rName.setText(restaurant.getName());
		restaurant_detail_price.setText("￥" + restaurant.getAvg_price() + "/人");
		restaurant_detail_navigation.setText(restaurant.getCity()
				+ restaurant.getArea()
				+ detailNavigation(restaurant.getNavigation()));
		restaurant_detail_tags.setText(detailTags(restaurant.getTags()));
		product_rating.setText("口味:" + restaurant.getProduct_rating());
		enviroment_rating.setText("环境:" + restaurant.getEnvironment_rating());
		service_rating.setText("服务:" + restaurant.getService_rating());
		restaurant_address.setText(restaurant.getAddress());
		restaurant_phone.setText(restaurant.getPhone());
		recommended_dishes.setText(restaurant.getRecommended_dishes());
		restaurant_detail_star
				.setImageResource(caculStar(restaurant.getStars()));
	}

	/**
	 * 分解地区
	 * */
	private String detailNavigation(String navigation) {
		String[] temp = navigation.split(">>");
		if (temp.length > 2) {
			return temp[2];
		}
		return "";
	}

	/**
	 * 分解类型
	 * */
	private String detailTags(String tags) {
		String[] temp = tags.split(",");
		if (temp.length >= 1) {
			return temp[0];
		}
		return "";
	}

	/**
	 * 
	 * @param star
	 *            传入的星级
	 * @return 应该设置的图片
	 */
	private int caculStar(float star) {
		int key = (int) (star * 10);
		switch (key) {
		case 10:
			return R.drawable.star10;
		case 20:
			return R.drawable.star20;
		case 30:
			return R.drawable.star30;
		case 35:
			return R.drawable.star35;
		case 40:
			return R.drawable.star40;
		case 45:
			return R.drawable.star45;
		case 50:
			return R.drawable.star50;
		}
		return R.drawable.star0;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.restaurant_phone:
			if (!restaurant.getPhone().equals("")) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ detailNumber(restaurant.getPhone())));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			break;
		case R.id.restaurant_address:
			if (latitude != 0 && longgitude != 0) {
				startNavi();
			}
			break;
		}
	}

	/**
	 * 提示未安装百度地图app或app版本过低
	 * */
	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				OpenClientUtil
						.getLatestBaiduMapApp(RestaurantDetailActivity.this);
			}
		});
		builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 启动百度地图导航
	 * */
	private void startNavi() {
		LatLng start = new LatLng(latitude, longgitude);
		LatLng end = new LatLng(restaurant.getLatitude(),
				restaurant.getLongitude());
		RouteParaOption para = new RouteParaOption().startPoint(start)
				.endPoint(end).endName(restaurant.getAddress())
				.busStrategyType(EBusStrategyType.bus_recommend_way);
		try {
			BaiduMapRoutePlan.openBaiduMapTransitRoute(para, this);
		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			showDialog();
		}
	}

	private String detailNumber(String number) {
		String[] temp = number.split("-");
		if (temp.length > 1) {
			String tnum = temp[0] + Long.parseLong(temp[1]);
			return tnum;
		} else {
			return number;
		}
	}
}

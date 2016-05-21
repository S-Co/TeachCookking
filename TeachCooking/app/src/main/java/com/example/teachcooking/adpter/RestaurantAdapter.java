package com.example.teachcooking.adpter;

import java.util.List;

import com.example.teachcooking.R;
import com.example.teachcooking.entity.Restaurant;
import com.example.teachcooking.util.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantAdapter extends BaseAdapter {
	private List<Restaurant> mRestaurants;
	private ImageLoader mImageLoader;
	private LayoutInflater inflater;

	public RestaurantAdapter(List<Restaurant> mRestaurants, Context context) {
		inflater = LayoutInflater.from(context);
		this.mRestaurants = mRestaurants;
		mImageLoader = new ImageLoader();
	}

	@Override
	public int getCount() {
		return mRestaurants.size();
	}

	@Override
	public Restaurant getItem(int position) {
		return mRestaurants.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RestaurantViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_around_item, null);
			holder = new RestaurantViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (RestaurantViewHolder) convertView.getTag();
		}
		Restaurant restaurant = mRestaurants.get(position);
		holder.around_item_avg_price.setText("￥" + restaurant.getAvg_price()
				+ "/人");
		holder.around_item_distance.setText("<3000m");
		holder.around_item_navigation.setText(restaurant.getCity()+restaurant.getArea()+detailNavigation(restaurant.getNavigation()));
		holder.around_item_rName.setText(restaurant.getName());
		holder.around_item_tags.setText(detailTags(restaurant.getTags()));
		holder.around_item_star.setImageResource(caculStar(restaurant.getStars()));
		//传入图片和ImageView对象 进行下载图片和设置图片
		holder.around_item_iv.setImageResource(R.drawable.ic_launcher);
		mImageLoader.displayImg(restaurant.getPhotos(), holder.around_item_iv);
		return convertView;
	}

	private class RestaurantViewHolder {
		ImageView around_item_iv;
		ImageView around_item_star;
		TextView around_item_rName;
		TextView around_item_avg_price;
		TextView around_item_navigation;
		TextView around_item_tags;
		TextView around_item_distance;

		public RestaurantViewHolder(View view) {
			around_item_iv = (ImageView) view.findViewById(R.id.around_item_iv);
			around_item_star = (ImageView) view.findViewById(R.id.around_item_star);
			around_item_rName = (TextView) view.findViewById(R.id.around_item_rName);
			around_item_avg_price = (TextView) view	.findViewById(R.id.around_item_avg_price);
			around_item_navigation = (TextView) view.findViewById(R.id.around_item_navigation);
			around_item_tags = (TextView) view.findViewById(R.id.around_item_tags);
			around_item_distance = (TextView) view.findViewById(R.id.around_item_distance);
		}
	}
	
	/**
	 * 分解地区
	 * */
	private String detailNavigation(String navigation){
		String [] temp =navigation.split(">>");
		if (temp.length>2) {
			return temp[2];
		}
		return "";
	}
	/**
	 * 分解类型
	 * */
	private String detailTags(String tags){
		String [] temp = tags.split(",");
		if (temp.length>=1) {
			return temp[0];
		}
		return "";
	}
/**
 * 
 * @param star 传入的星级
 * @return 应该设置的图片
 */
	private int caculStar(float star) {
		int key = (int) (star*10);
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
}

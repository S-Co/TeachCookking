package com.example.teachcooking.adpter;

import java.util.List;

import com.example.teachcooking.R;
import com.example.teachcooking.entity.CookBookInfo;
import com.example.teachcooking.util.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class MyPublishProductAdapter extends BaseAdapter{
	private Context context;
	private List<CookBookInfo> cookBookInfos;
	private ImageLoader mImageLoader;
	public MyPublishProductAdapter(Context context,
			List<CookBookInfo> cookBookInfos) {
		this.context = context;
		this.cookBookInfos = cookBookInfos;
		mImageLoader = new ImageLoader();
	}

	@Override
	public int getCount() {
		return cookBookInfos.size();
	}

	@Override
	public CookBookInfo getItem(int position) {
		return cookBookInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.my_pulish_product_item, null);
		ImageView view = (ImageView) convertView.findViewById(R.id.my_pulish_product_item_iv);
		view.setScaleType(ScaleType.CENTER_CROP);
		CookBookInfo cookBookInfo = cookBookInfos.get(position);
		String imgUrl = cookBookInfo.getStep_pics().get(0).getUrl();
		mImageLoader.displayImg(imgUrl, view);
		return convertView;
	}

	public List<CookBookInfo> getCookBookInfos() {
		return cookBookInfos;
	}

	public void setCookBookInfos(List<CookBookInfo> cookBookInfos) {
		if (this.cookBookInfos!=null) {
			this.cookBookInfos.clear();
		}
		this.cookBookInfos = cookBookInfos;
			notifyDataSetChanged();
	}

}

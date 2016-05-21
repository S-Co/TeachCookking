package com.example.teachcooking.adpter;

import java.util.List;

import com.example.teachcooking.R;
import com.example.teachcooking.TeachDetailActivity;
import com.example.teachcooking.entity.CookBookInfo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BasketActivityAdapter extends BaseAdapter{
	private List<CookBookInfo> cookBookInfos;
	private Context context;
	private LayoutInflater inflater;
	public BasketActivityAdapter(List<CookBookInfo> cookBookInfos,
			Context context) {
		this.cookBookInfos = cookBookInfos;
		this.context = context;
		inflater = LayoutInflater.from(context);
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
		BasketActivityAdapterViewHolder viewHolder;
		if (convertView==null) {
			convertView = inflater.inflate(R.layout.activity_basket_item, null);
			viewHolder = new BasketActivityAdapterViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (BasketActivityAdapterViewHolder) convertView.getTag();
		}
		final CookBookInfo cookBookInfo = cookBookInfos.get(position);
		viewHolder.activity_basket_item_cookbookname.setText(cookBookInfo.getCookBookName());
		viewHolder.basket_material.setText(cookBookInfo.getCookBookMaterial());
		viewHolder.activity_basket_item_cookbookname.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,TeachDetailActivity.class);
				intent.putExtra("cookBookInfo", cookBookInfo);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	private class BasketActivityAdapterViewHolder{
		TextView activity_basket_item_cookbookname;
		TextView basket_material;
		public BasketActivityAdapterViewHolder(View view){
			activity_basket_item_cookbookname = (TextView) view.findViewById(R.id.activity_basket_item_cookbookname);
			basket_material = (TextView) view.findViewById(R.id.basket_material);
		}
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

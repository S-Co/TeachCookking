package com.example.teachcooking.adpter;

import java.util.List;

import com.example.teachcooking.R;
import com.example.teachcooking.entity.ResponseClassifys.Cuisine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class CookCategoryGridViewAdapter extends BaseAdapter{

	private List<Cuisine> cuisines;
	private LayoutInflater mInflater;
	
	public CookCategoryGridViewAdapter(List<Cuisine> cuisines, Context context) {
		super();
		this.cuisines = cuisines;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return cuisines.size();
	}

	@Override
	public Cuisine getItem(int position) {
		return cuisines.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_cook_category_gridview, null);
			holder = new ViewHolder();
			holder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.nameTv.setText(getItem(position).getName());
		return convertView;
	}

	private static final class ViewHolder{
		TextView nameTv;
	}
	
}

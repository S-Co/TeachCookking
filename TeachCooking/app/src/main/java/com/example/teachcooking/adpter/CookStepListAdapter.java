package com.example.teachcooking.adpter;

import java.util.List;

import com.example.teachcooking.R;
import com.example.teachcooking.entity.CookStep;
import com.example.teachcooking.util.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CookStepListAdapter extends BaseAdapter{

	private List<CookStep> cookSteps;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader; 
	public CookStepListAdapter(Context context,List<CookStep> cookSteps) {
		super();
		mInflater = LayoutInflater.from(context);
		this.cookSteps = cookSteps;
		mImageLoader = new ImageLoader();
	}

	@Override
	public int getCount() {
		return cookSteps.size();
	}

	@Override
	public CookStep getItem(int position) {
		// TODO Auto-generated method stub
		return cookSteps.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_cook_step, null);
			holder = new ViewHolder();
			holder.stepNum = (TextView) convertView.findViewById(R.id.step_num);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		CookStep cookStep = getItem(position);
		holder.stepNum.setText(position+1+"");
		holder.text.setText(cookStep.getStep().substring(2));
		holder.img.setImageResource(R.drawable.default_list_pic);
		mImageLoader.displayImg(cookStep.getImg(), holder.img);
		return convertView;
	}

	private class ViewHolder{
		TextView stepNum;
		ImageView img;
		TextView text;
	}
}

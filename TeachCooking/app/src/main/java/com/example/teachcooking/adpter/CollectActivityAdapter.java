package com.example.teachcooking.adpter;

import java.util.List;

import com.example.teachcooking.R;
import com.example.teachcooking.entity.CookBookInfo;
import com.example.teachcooking.entity.UsuerInfo;
import com.example.teachcooking.util.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CollectActivityAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<CookBookInfo> cookBookInfos;
	private ImageLoader mImageLoader;

	public CollectActivityAdapter(Context context,
			List<CookBookInfo> cookBookInfos) {
		this.cookBookInfos = cookBookInfos;
		this.setContext(context);
		inflater = LayoutInflater.from(context);
		mImageLoader = new ImageLoader();
	}
	public List<CookBookInfo> getCookBookInfos() {
		return cookBookInfos;
	}

	public void setCookBookInfos(List<CookBookInfo> cookBookInfos) {
		if (cookBookInfos != null) {
			this.cookBookInfos.clear();
		}
		this.cookBookInfos = cookBookInfos;
		notifyDataSetChanged();
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
		CollectActivityViewHolder viewHolder;
		if (convertView==null) {
			convertView = inflater.inflate(R.layout.collect_items, null);
			viewHolder = new CollectActivityViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (CollectActivityViewHolder) convertView.getTag();
		}
		CookBookInfo cookBookInfo = cookBookInfos.get(position);
		UsuerInfo usuerInfo = cookBookInfo.getAuthor();
		String pic = cookBookInfo.getStep_pics().get(0).getUrl();
		mImageLoader.displayImg(pic, viewHolder.collect_cookbookpic);
		viewHolder.collect_cookbookname.setText(cookBookInfos.get(position).getCookBookName());
		viewHolder.publish_nick.setText(usuerInfo.getNick());
		return convertView;
	}

	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}

	private  class CollectActivityViewHolder{
		ImageView collect_cookbookpic;
		TextView collect_cookbookname;
		TextView publish_nick;
		public CollectActivityViewHolder(View view){
			collect_cookbookpic = (ImageView) view.findViewById(R.id.collect_cookbookpic);
			collect_cookbookname = (TextView) view.findViewById(R.id.collect_cookbookname);
			publish_nick = (TextView) view.findViewById(R.id.publish_nick);
		}
	}
}

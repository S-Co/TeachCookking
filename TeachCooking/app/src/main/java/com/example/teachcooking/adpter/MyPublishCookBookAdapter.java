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

public class MyPublishCookBookAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	public List<CookBookInfo> getCookBookInfos() {
		return cookBookInfos;
	}
	public void setCookBookInfos(List<CookBookInfo> cookBookInfos) {
		if (cookBookInfos!=null) {
			this.cookBookInfos.clear();			
		}
		this.cookBookInfos = cookBookInfos;
		notifyDataSetChanged();
	}
	private List<CookBookInfo>  cookBookInfos;
	private ImageLoader mImageLoader;
	public MyPublishCookBookAdapter(Context context,List<CookBookInfo>  cookBookInfos) {
		this.cookBookInfos = cookBookInfos;
		this.setContext(context);
		inflater = LayoutInflater.from(context);
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
		MyPublishAdapterViewHolder viewHolder;
		if (convertView==null) {
			convertView = inflater.inflate(R.layout.my_pulish_cookbook, null);
			viewHolder = new MyPublishAdapterViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (MyPublishAdapterViewHolder) convertView.getTag();
		}
		CookBookInfo cookBookInfo = cookBookInfos.get(position);
		UsuerInfo usuerInfo = cookBookInfo.getAuthor();
		String usuerUrl = usuerInfo.getPic().getUrl();
		String cookbookpicUrl = cookBookInfo.getStep_pics().get(0).getUrl();
		mImageLoader.displayImg(usuerUrl, viewHolder.my_publish_pic);
		mImageLoader.displayImg(cookbookpicUrl, viewHolder.my_publish_cookBookPic);
		viewHolder.my_publish_cookbookname.setText(cookBookInfo.getCookBookName());
		viewHolder.my_publish_nick.setText(usuerInfo.getNick());
		return convertView;
	}
		public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
		private class MyPublishAdapterViewHolder{
			ImageView my_publish_cookBookPic;
			ImageView my_publish_pic;
			TextView my_publish_cookbookname;
			TextView my_publish_nick;
			public MyPublishAdapterViewHolder(View view){
				my_publish_cookbookname = (TextView) view.findViewById(R.id.my_publish_cookbookname);
				my_publish_pic = (ImageView) view.findViewById(R.id.my_publish_pic);
				my_publish_cookBookPic = (ImageView) view.findViewById(R.id.my_publish_cookBookPic);
				my_publish_nick = (TextView) view.findViewById(R.id.my_publish_nick);
			}
		}
}

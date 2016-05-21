package com.example.teachcooking.adpter;

import java.util.List;

import cn.bmob.v3.BmobUser;

import com.example.teachcooking.R;
import com.example.teachcooking.UserDetailActivity;
import com.example.teachcooking.entity.CookBookInfo;
import com.example.teachcooking.entity.UsuerInfo;
import com.example.teachcooking.util.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TeachAdapter extends BaseAdapter {
	private List<CookBookInfo> cookBookInfos;
	private LayoutInflater inflater;
	private Context context;
	private ImageLoader mImageLoader;
	private UsuerInfo currentUser;

	public TeachAdapter(List<CookBookInfo> cookBookInfos, Context context) {
		this.cookBookInfos = cookBookInfos;
		this.context = context;
		mImageLoader = new ImageLoader();
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
		TeachViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.teaching_item, null);
			holder = new TeachViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (TeachViewHolder) convertView.getTag();
		}
		CookBookInfo cookBookInfo = cookBookInfos.get(position);
		final UsuerInfo usuerInfo = cookBookInfo.getAuthor();
		String cookBookPicName = cookBookInfo.getStep_pics().get(0).getUrl();
		String publishPicName = usuerInfo.getPic().getUrl();
		mImageLoader.displayImg(cookBookPicName, holder.cookBookPic);
		mImageLoader.displayImg(publishPicName, holder.publish_pic);
		holder.publish_time.setText("发布于" + cookBookInfo.getCreatedAt());
		holder.cookbookname.setText(cookBookInfo.getCookBookName());
		holder.publish_usuername.setText(usuerInfo.getNick());
		currentUser = BmobUser.getCurrentUser(context, UsuerInfo.class);
		holder.publish_pic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentUser != null) {
					Intent intent = new Intent(context,
							UserDetailActivity.class);
					intent.putExtra("usuerInfo", usuerInfo);
					context.startActivity(intent);
				}
			}
		});
		return convertView;
	}

	private class TeachViewHolder {
		private ImageView cookBookPic;
		private ImageView publish_pic;
		private TextView cookbookname;
		private TextView publish_usuername;
		private TextView publish_time;

		private TeachViewHolder(View view) {
			publish_time = (TextView) view.findViewById(R.id.publish_time);
			cookBookPic = (ImageView) view.findViewById(R.id.cookBookPic);
			publish_pic = (ImageView) view.findViewById(R.id.publish_pic);
			cookbookname = (TextView) view.findViewById(R.id.cookbookname);
			publish_usuername = (TextView) view
					.findViewById(R.id.publish_usuername);
		}
	}

	/**
	 * 添加数据
	 * 
	 * @param cookBooks
	 */
	public void addDatas(List<CookBookInfo> cookBookInfos) {
		this.cookBookInfos.addAll(cookBookInfos);
		notifyDataSetChanged();
	}

	/**
	 * 更新数据
	 * 
	 * @param cookBooks
	 */
	public void updateDatas(List<CookBookInfo> cookBookInfos) {
		this.cookBookInfos.clear();
		this.cookBookInfos.addAll(cookBookInfos);
		notifyDataSetChanged();
	}
}

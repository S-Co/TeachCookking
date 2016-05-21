package com.example.teachcooking.adpter;

import java.util.ArrayList;
import java.util.List;

import com.example.teachcooking.CookListActivity;
import com.example.teachcooking.R;
import com.example.teachcooking.entity.ResponseClassifys.Classify;
import com.example.teachcooking.entity.ResponseClassifys.Cuisine;
import com.example.teachcooking.view.CookCategoryGridView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
/**
 * ȫ�����׷����Adapter
 * @author Li
 *
 */
public class CookCategoryListAdapter extends BaseAdapter{
	private List<Classify> classifys;
	private LayoutInflater mInflater;
	private List<CookCategoryGridViewAdapter> mAdapters;
	private List<LayoutParams> mParams;
	private Context mContext;
	public CookCategoryListAdapter(List<Classify> classifys, Context context) {
		super();
		mContext = context;
		this.classifys = classifys;
		mInflater = LayoutInflater.from(context);
		
		mAdapters = new ArrayList<CookCategoryGridViewAdapter>();
		mParams = new ArrayList<LayoutParams>();
		measureHeight();
		for (int i = 0;i < classifys.size();i++) {
			mAdapters.add(new CookCategoryGridViewAdapter(classifys.get(i).getList(), context));
			
			mParams.add(new LayoutParams(LayoutParams.MATCH_PARENT,getItemHeight(classifys.get(i).getList().size())));
		}
	}

	@Override
	public int getCount() {
		return classifys.size();
	}

	@Override
	public Classify getItem(int position) {
		return classifys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_cook_category, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.gridView = (GridView) convertView.findViewById(R.id.gridView);
			holder.gridView.setOnItemClickListener(onItemClickListener);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Classify classify = getItem(position);
//		holder.params.height = getItemHeight(classify.getList().size());
		convertView.setLayoutParams(mParams.get(position));
		holder.title.setText(classify.getName());
		holder.gridView.setAdapter(mAdapters.get(position));
		return convertView;
	}
	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			CookCategoryGridViewAdapter adapter = (CookCategoryGridViewAdapter) parent.getAdapter();
			Cuisine cuisine = adapter.getItem(position);
			Intent intent = new Intent(mContext, CookListActivity.class);
			intent.putExtra("title", cuisine.getName());
			intent.putExtra("cid", cuisine.getId());
			mContext.startActivity(intent);
		}
	};

	private int mTitleHeight;
	private int mGridViewItemHeight;
	private void measureHeight(){
		View titleView = mInflater.inflate(R.layout.item_cook_category, null);
		titleView.measure(0, 0);
		mTitleHeight = titleView.getMeasuredHeight();
		View mGridViewItem = mInflater.inflate(R.layout.item_cook_category_gridview, null);
		mGridViewItem.measure(0, 0);
		mGridViewItemHeight = mGridViewItem.getMeasuredHeight();
	}
	private int getItemHeight(int itemCount){
		int itemLineCount = itemCount/CookCategoryGridView.NUM_COLUMNS;
		if(itemCount % CookCategoryGridView.NUM_COLUMNS != 0){
			itemLineCount++;
		}
		return mTitleHeight + (itemLineCount-1)*CookCategoryGridView.VERTICAL_SPACING + itemLineCount*mGridViewItemHeight;
	}
	
	private static final class ViewHolder{
		TextView title;
		GridView gridView;
//		LayoutParams params;
	}
	
}

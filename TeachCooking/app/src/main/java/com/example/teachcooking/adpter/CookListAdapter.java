package com.example.teachcooking.adpter;

import java.util.ArrayList;
import java.util.List;

import com.example.teachcooking.R;
import com.example.teachcooking.entity.CookBook;
import com.example.teachcooking.util.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class CookListAdapter extends BaseAdapter{

	private List<CookBook> mCookBooks;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	
	private List<Integer> selectedPositions;
	
	public List<Integer> getSelectedPositions() {
		return selectedPositions;
	}
	private boolean mIdModeEdit;
	public boolean ismIdModeEdit() {
		return mIdModeEdit;
	}

	public void setmIdModeEdit(boolean mIdModeEdit) {
		this.mIdModeEdit = mIdModeEdit;
		notifyDataSetChanged();
	}

	public CookListAdapter(List<CookBook> cookBooks, Context context) {
		super();
		this.mCookBooks = cookBooks;
		mInflater = LayoutInflater.from(context);
		mImageLoader = new ImageLoader();
		selectedPositions = new ArrayList<Integer>();
	}

	@Override
	public int getCount() {
		return mCookBooks.size();
	}

	@Override
	public CookBook getItem(int position) {
		return mCookBooks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_cooks_list, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.cookNameText = (TextView) convertView.findViewById(R.id.cook_name);
			holder.cookTagsText = (TextView) convertView.findViewById(R.id.cook_tags);
			holder.cooKIngredientsText = (TextView) convertView.findViewById(R.id.cook_ingredients);
			holder.cooBurdenText = (TextView) convertView.findViewById(R.id.cook_burden);
			holder.checkImg = (ImageView) convertView.findViewById(R.id.checkImg);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		CookBook cookBook = getItem(position);
		holder.cookNameText.setText(cookBook.getTitle());
		holder.cookTagsText.setText(cookBook.getTags());
		holder.cooKIngredientsText.setText(cookBook.getIngredients());
		holder.cooBurdenText.setText(cookBook.getBurden());
		holder.img.setImageResource(R.drawable.default_list_pic);
		mImageLoader.displayImg(cookBook.getAlbums().get(0), holder.img);
		
		if(mIdModeEdit){
			holder.checkImg.setVisibility(View.VISIBLE);
			if(selectedPositions.contains(position)){
				holder.checkImg.setImageResource(R.drawable.check_in);
			}else{
				holder.checkImg.setImageResource(R.drawable.check_out);
			}
		}else{
			holder.checkImg.setVisibility(View.GONE);
		}
		return convertView;
	}

	private static final class ViewHolder{
		ImageView img;
		TextView cookNameText;
		TextView cookTagsText;
		TextView cooKIngredientsText;
		TextView cooBurdenText;
		ImageView checkImg;
	}
	public static final void log(Object o){
		Log.d(CookListAdapter.class.getName(), o+"");
	}
	
	/**
	 * �������
	 * @param cookBooks
	 */
	public void addDatas(List<CookBook> cookBooks){
		this.mCookBooks.addAll(cookBooks);
		notifyDataSetChanged();
	}
	/**
	 * ��������
	 * @param cookBooks
	 */
	public void updateDatas(List<CookBook> cookBooks){
		this.mCookBooks.clear();
		this.mCookBooks.addAll(cookBooks);
		notifyDataSetChanged();
	}
	
	/**
	 * �ı�ָ��λ��Item��ѡ��״̬
	 * @param position
	 */
	public void toggleItem(int position){
		if(selectedPositions.contains(position)){
			selectedPositions.remove((Integer)position);
		}else{
			selectedPositions.add(position);
		}
		notifyDataSetChanged();
		if(onItemSelectedChangeListener != null){
			if(selectedPositions.size() == getCount()){
				onItemSelectedChangeListener.onItemAllSelectedChange(true);
			}else{
				onItemSelectedChangeListener.onItemAllSelectedChange(false);
			}
		}
		System.out.println(selectedPositions.size()+"--"+getCount());
	}
	
	/**
	 * ѡ������
	 */
	public void selectAll(){
		selectedPositions.clear();
		for (int i = 0; i < mCookBooks.size(); i++) {
			selectedPositions.add(i);
		}
		notifyDataSetChanged();
	}
	/**
	 * ȡ������ѡ��
	 */
	public void unSelectAll(){
		selectedPositions.clear();
		notifyDataSetChanged();
	}
	
	
	public interface OnItemSelectedChangeListener{
		void onItemAllSelectedChange(boolean allSelected);
	}
	private OnItemSelectedChangeListener onItemSelectedChangeListener;
	public void setOnItemSelectedChangeListener(
			OnItemSelectedChangeListener onItemSelectedChangeListener) {
		this.onItemSelectedChangeListener = onItemSelectedChangeListener;
	}

	/**
	 * ��ȡѡ�е����в���id����
	 * @return
	 */
	public List<String> getCookBookIds() {
		List<String> cookBookIds = new ArrayList<String>();
		for (Integer position : selectedPositions) {
			cookBookIds.add(mCookBooks.get(position).getId());
		}
		return cookBookIds;
	}
	
}

package com.example.teachcooking.fragment;

import java.util.List;

import com.example.teachcooking.CookDetailsActivity;
import com.example.teachcooking.R;
import com.example.teachcooking.adpter.CookListAdapter;
import com.example.teachcooking.db.DbHelper;
import com.example.teachcooking.entity.CookBook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragmentNearby extends Fragment{
	private View mContentView;
	private ListView mListView;
	private CookListAdapter mListAdapter;
	private DbHelper mHelper;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_nearby, null);
		return mContentView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mHelper = new DbHelper(getActivity());
		initListView();
	}
	
	/**
	 * ��ʼ��ListView
	 */
	private void initListView() {
		mListView = (ListView) mContentView.findViewById(R.id.listView);
		cookBooks = mHelper.getNearByCookBooks(null);
		mListAdapter = new CookListAdapter(cookBooks , getActivity());
		View emptyView = mContentView.findViewById(R.id.empty_view);
		mListView.setEmptyView(emptyView );
		mListView.setAdapter(mListAdapter);mListView.setOnItemClickListener(onItemClickListener);
	}
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			CookBook cookBook = mListAdapter.getItem(position);
			Intent intent = new Intent(getActivity(), CookDetailsActivity.class);
			intent.putExtra("cookBook", cookBook);
			getActivity().startActivity(intent);
		}
	};
	private List<CookBook> cookBooks;
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(!hidden){
			List<CookBook> nearByCookBooks = mHelper.getNearByCookBooks(null);
			if(cookBooks.size() != nearByCookBooks.size()){
				mListAdapter.updateDatas(nearByCookBooks);
				mListView.setSelection(0);
			}
		}
	}
	
	
}

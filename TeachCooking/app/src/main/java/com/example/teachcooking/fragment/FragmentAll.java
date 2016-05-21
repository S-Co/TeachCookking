package com.example.teachcooking.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.teachcooking.CookDetailsActivity;
import com.example.teachcooking.R;
import com.example.teachcooking.adpter.CookListAdapter;
import com.example.teachcooking.base.LoadFragment;
import com.example.teachcooking.entity.CookBook;
import com.example.teachcooking.entity.ResponseCookBooks;
import com.example.teachcooking.util.HttpUtil;
import com.example.teachcooking.util.HttpUtil.CallBack;
import com.example.teachcooking.util.MyConstant;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FragmentAll extends LoadFragment{
	private ListView mListView;
	private CookListAdapter mListAdapter;
	private String cid;
	private String search_key;
	private String url;
	private int mCurPage = -1;
	private int mTotalDataCount;
	private View mNoSearchView;
	public void setCid(String cid) {
		this.cid = cid;
	}
	public void setSearch_key(String search_key) {
		this.search_key = search_key;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_all, null);
		return mContentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		System.out.println("onActivityCreated");
		if(TextUtils.isEmpty(cid)){
			url = MyConstant.URL_SEARCH.replace("search_key", search_key);
		}else{
			url = MyConstant.URL_NORMAL.replace("cid_num", cid);
		}
		initViews();
		loadDataFirst();
	}
	
	private View mFooterView;
	private ProgressBar mFooterProgressBar;
	private TextView mFooterText;
	private void initViews() {
		mListView = (ListView) mContentView.findViewById(R.id.listView);
		mListAdapter = new CookListAdapter(new ArrayList<CookBook>(), getActivity());
		mFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_auto_load_listview, null);
		mFooterProgressBar = (ProgressBar) mFooterView.findViewById(R.id.footer_progressbar);
		mFooterText = (TextView) mFooterView.findViewById(R.id.footer_text);
		mListView.addFooterView(mFooterView);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(onItemClickListener);
		mNoSearchView = mContentView.findViewById(R.id.no_search);
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


		@Override
		protected void loadDataFirst() {
			loading();
			loadData();
		}

		private boolean mIsLoading = false;
		
		private String getUrl(int page){
			return url.replace("page", ""+page);
		}
		
		private boolean allLoaded = false;
		private void loadData(){
			if(mIsLoading){
				return;
			}
			mCurPage++;
			mIsLoading = true;
			new HttpUtil().get(getUrl(mCurPage), new CallBack() {

				@Override
				public void onSuccess(String result) {
					if(isFinish){
						return;
					}
					ResponseCookBooks response = new Gson().fromJson(result, ResponseCookBooks.class);
					if(!"200".equals(response.getResultcode())){
						mNoSearchView.setVisibility(View.VISIBLE);
						loadSuccess();
						return;
					}
					if(mTotalDataCount == 0){
						mTotalDataCount = Integer.parseInt(response.getResult().getTotalNum());
					}
					List<CookBook> list = response.getResult().getData();
					if(mListAdapter.getCount() + list.size() >= mTotalDataCount){
						allLoaded = true;
						mFooterProgressBar.setVisibility(View.GONE);
						mFooterText.setText("ľ�и���������~");
						mListAdapter.addDatas(list.subList(0, mTotalDataCount - mListAdapter.getCount()));
					}else{
						mListAdapter.addDatas(response.getResult().getData());
					}
					loadSuccess();
					mIsLoading = false;
					System.out.println(mListAdapter.getCount()+"--"+mTotalDataCount);
					mListView.setVisibility(View.VISIBLE);
				}
				@Override
				public void onFailure() {
					loadFailue();
					mIsLoading = false;
				}
			});
		}
}

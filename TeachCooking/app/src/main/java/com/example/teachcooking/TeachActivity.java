package com.example.teachcooking;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import com.example.teachcooking.adpter.TeachAdapter;
import com.example.teachcooking.entity.BreakfastInfo;
import com.example.teachcooking.entity.CookBookInfo;
import com.example.teachcooking.entity.DinnerInfo;
import com.example.teachcooking.entity.LunchInfo;
import com.example.teachcooking.entity.ResponseClassifys;
import com.example.teachcooking.entity.UsuerInfo;
import com.example.teachcooking.util.HttpUtil;
import com.example.teachcooking.util.MyConstant;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TeachActivity extends Fragment implements OnClickListener,
		OnItemClickListener {
	private ImageView publish_cookbook;
	private ListView teach_listview;
	private TeachAdapter adapter;
	private int mCurPage = 0;// 当前查询的条（开始条）
	private int mTotalDataCount;// 当前的查询的条件所有数据大小
	// 加载更多
	private View mFooterView;
	private ProgressBar mFooterProgressBar;
	private TextView mFooterText;
	// header
	private TextView cookbooks;
	private TextView breakfast;
	private TextView lunch;
	private TextView dinner;
	private ResponseClassifys mResponseClassifys;
	private ImageView mSearchImg;
	private EditText teaching_search;
	public int firstVisibleItem;
	private float firstY;
	// 午餐信息
	private LunchInfo lunchInfo;
	// 早餐信息
	private BreakfastInfo breakfastInfo;
	// 晚餐信息
	private DinnerInfo dinnerInfo;
	private SwipeRefreshLayout swipeRefreshLayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_teaching, null);
		loadLocalData();
		initView(view);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		afterLogicView();
		query();
	}


	private void afterLogicView() {
		adapter = new TeachAdapter(new ArrayList<CookBookInfo>(), getActivity());
		teach_listview.setAdapter(adapter);
		teach_listview.setOnScrollListener(new OnScrollListener() {
			private int lastItemIndex;// 当前ListView中最后一个Item的索引

			// 当ListView不在滚动，并且ListView的最后一项的索引等于adapter的项数减一时则自动加载（因为索引是从0开始的）
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastItemIndex == adapter.getCount() - 1) {
					loadData();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastItemIndex = firstVisibleItem + visibleItemCount - 1 - 1;
				TeachActivity.this.firstVisibleItem = firstVisibleItem;

			}
		});
		teach_listview.setOnItemClickListener(this);
		loadData();
	}

	// 输入监听
	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// 没有内容时，搜索图表隐藏，内容改变，现实搜索图表
			if (TextUtils.isEmpty(s)) {
				mSearchImg.setClickable(false);
			} else {
				mSearchImg.setClickable(true);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	private void initView(View view) {
		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
		swipeRefreshLayout.setColorScheme(R.color.DeepSkyBlue,R.color.Red,R.color.DarkOrange,R.color.SpringGreen);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mCurPage = 0;
				afterLogicView();
				new android.os.Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						//处理下拉刷新
						swipeRefreshLayout.setRefreshing(false);  //下拉刷新结束
					}
				}, 2000);
			}
		});
		teaching_search = (EditText) view.findViewById(R.id.teaching_search);
		InputMethodManager manager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.hideSoftInputFromWindow(teaching_search.getWindowToken(), 0);
		mSearchImg = (ImageView) view.findViewById(R.id.img_search);
		mSearchImg.setOnClickListener(this);
		teaching_search.addTextChangedListener(watcher);
		publish_cookbook = (ImageView) view.findViewById(R.id.publish_cookbook);
		teach_listview = (ListView) view.findViewById(R.id.teach_listview);
		publish_cookbook.setOnClickListener(this);
		cookbooks = (TextView) view.findViewById(R.id.cookbooks);
		breakfast = (TextView) view.findViewById(R.id.breakfast);
		lunch = (TextView) view.findViewById(R.id.lunch);
		dinner = (TextView) view.findViewById(R.id.dinner);
		breakfast.setOnClickListener(this);
		lunch.setOnClickListener(this);
		dinner.setOnClickListener(this);
		cookbooks.setOnClickListener(this);
		mFooterView = LayoutInflater.from(getActivity()).inflate(
				R.layout.footer_auto_load_listview, null);
		mFooterProgressBar = (ProgressBar) mFooterView
				.findViewById(R.id.footer_progressbar);
		mFooterText = (TextView) mFooterView.findViewById(R.id.footer_text);
		teach_listview.addFooterView(mFooterView);// 添加 加载更多
	}

	@Override
	public void onStop() {
		mCurPage = 0;
		super.onStop();
	}

	private boolean mIsLoading = false;// 判断不在没有加载完内容的情况下 重新加载

	// 加载数据的方法
	private void loadData() {
		if (mIsLoading) {
			return;
		}
		mIsLoading = true;
		if (mTotalDataCount == 0) {
			BmobQuery<CookBookInfo> query = new BmobQuery<CookBookInfo>();
			query.setLimit(100);
			query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
			query.count(getActivity(), CookBookInfo.class, new CountListener() {

				@Override
				public void onFailure(int code, String msg) {
					Toast.makeText(getActivity(), "错误" + code + "信息" + msg,
							Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onSuccess(int count) {
					// 获得总共该类查询总共数
					mTotalDataCount = count;
					if (mTotalDataCount == 0) {
						mFooterProgressBar.setVisibility(View.GONE);
						mFooterText.setVisibility(View.VISIBLE);
						mFooterText.setText("木有数据~");
						return;
					} else {
						queryCookBookInfo();
					}
				}
			});
		} else {
			queryCookBookInfo();
		}
	}

	private void query() {
		BmobQuery<LunchInfo> query = new BmobQuery<LunchInfo>();
		query.setLimit(1);
		query.include("author");
		query.order("-createdAt");
		query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		query.findObjects(getActivity(), new FindListener<LunchInfo>() {
			@Override
			public void onError(int arg0, String arg1) {
			}

			@Override
			public void onSuccess(List<LunchInfo> arg0) {
				lunchInfo = arg0.get(0);
			}
		});
		BmobQuery<BreakfastInfo> query2 = new BmobQuery<BreakfastInfo>();
		query2.setLimit(1);
		query2.include("author");
		query2.order("-createdAt");
		query2.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		query2.findObjects(getActivity(), new FindListener<BreakfastInfo>() {
			@Override
			public void onSuccess(List<BreakfastInfo> arg0) {
				breakfastInfo = arg0.get(0);
			}

			@Override
			public void onError(int arg0, String arg1) {
			}
		});
		BmobQuery<DinnerInfo> query3 = new BmobQuery<DinnerInfo>();
		query3.setLimit(1);
		query3.include("author");
		query3.order("-createdAt");
		query3.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		query3.findObjects(getActivity(), new FindListener<DinnerInfo>() {
			@Override
			public void onSuccess(List<DinnerInfo> arg0) {
				dinnerInfo = arg0.get(0);
			}

			@Override
			public void onError(int arg0, String arg1) {
			}
		});
	}

	private void queryCookBookInfo() {
		BmobQuery<CookBookInfo> query = new BmobQuery<CookBookInfo>();
		query.setLimit(3);
		query.include("author");
		query.setSkip(mCurPage);
		query.order("-createdAt");
		query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		mCurPage += 3;// 条数 每次往后移10行
		query.findObjects(getActivity(), new FindListener<CookBookInfo>() {

			@Override
			public void onSuccess(List<CookBookInfo> object) {
				List<CookBookInfo> list = object;
				// 判断 当前listVIew中加上本次内容 如果大于或者等于了 该类的总数 则取消加载更多 显示没有更多数据
				// 并加载本次的数据
				if (adapter.getCount() + list.size() >= mTotalDataCount) {
					mFooterProgressBar.setVisibility(View.GONE);
					mFooterText.setVisibility(View.VISIBLE);
					mFooterText.setText("木有更多数据啦~");
					adapter.addDatas(list.subList(0,
							mTotalDataCount - adapter.getCount()));
				} else {
					adapter.addDatas(list);
					mFooterText.setVisibility(View.GONE);
				}
				// 可以进行下次的内容加载
				mIsLoading = false;
			}

			@Override
			public void onError(int code, String msg) {
				Toast.makeText(getActivity(), "错误" + code + "信息" + msg,
						Toast.LENGTH_SHORT).show();
				mCurPage -= 3;//
				return;
			}
		});
	}

	@Override
	public void onClick(View v) {
		UsuerInfo user = BmobUser
				.getCurrentUser(getActivity(), UsuerInfo.class);
		switch (v.getId()) {
		case R.id.publish_cookbook:
			if (user!=null) {
				Intent intent = new Intent(getActivity(),
						PublishCookbookActivity.class);
				startActivity(intent);				
			}else {
				Toast.makeText(getActivity(), "请先登录才能发布菜谱！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.cookbooks:
			Intent intentcookbook = new Intent(getActivity(),
					AllCookCategoryActivity.class);
			intentcookbook.putExtra("data", mResponseClassifys);
			startActivity(intentcookbook);
			break;
		case R.id.img_search:// 搜索监听
			String text = teaching_search.getText().toString();
			text = text.replace(" ", "");
			if (text.length() != 0) {
				// 跳转页面搜索
				Intent searchIntent = new Intent(getActivity(),
						CookListActivity.class);
				searchIntent.putExtra("title", "菜谱搜索");
				searchIntent.putExtra("search_key", text);
				searchIntent.putExtra("tag", MyConstant.TAG_SEARCH);
				startActivity(searchIntent);
			}
			break;
		case R.id.lunch:
			if (user != null) {
				Intent lunchIntent = new Intent(getActivity(),
						TeachDetailActivity.class);
				lunchIntent.putExtra("lunchInfo", lunchInfo);
				startActivity(lunchIntent);
			}else {
				Toast.makeText(getActivity(), "请先登录才能查看推荐午餐信息", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.breakfast:
			if (user != null) {
				Intent breakfastIntent = new Intent(getActivity(),
						TeachDetailActivity.class);
				breakfastIntent.putExtra("breakfastInfo", breakfastInfo);
				startActivity(breakfastIntent);
			}else {
				Toast.makeText(getActivity(), "请先登录才能查看推荐早餐信息", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.dinner:
			if (user != null) {
				Intent dinnerIntent = new Intent(getActivity(),
						TeachDetailActivity.class);
				dinnerIntent.putExtra("dinnerInfo", dinnerInfo);
				startActivity(dinnerIntent);
			}else {
				Toast.makeText(getActivity(), "请先登录才能查看推荐晚餐信息", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	// 加载数据到适配器
	private void loadLocalData() {
		InputStream is = getResources().openRawResource(R.raw.cooks_classify);
		try {
			String str = HttpUtil.readStream(is);
			loadSuccess(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 解析数据并更新到适配器中
	@SuppressLint("NewApi")
	private void loadSuccess(String result) {
		// 解析数据，转成对象
		mResponseClassifys = new Gson().fromJson(result,
				ResponseClassifys.class);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		UsuerInfo user = BmobUser
				.getCurrentUser(getActivity(), UsuerInfo.class);
		if (user != null) {
			CookBookInfo cookBookInfo = adapter.getItem(position);
			Intent intent = new Intent(getActivity(), TeachDetailActivity.class);
			intent.putExtra("cookBookInfo", cookBookInfo);
			startActivity(intent);
		}else {
			Toast.makeText(getActivity(), "请先登录才能查看菜谱详情", Toast.LENGTH_SHORT).show();
		}
	}

}

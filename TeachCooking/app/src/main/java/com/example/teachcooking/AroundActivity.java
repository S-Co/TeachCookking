package com.example.teachcooking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.example.teachcooking.adpter.RestaurantAdapter;
import com.example.teachcooking.entity.ResponseClass;
import com.example.teachcooking.entity.Restaurant;
import com.example.teachcooking.view.LoadingImgView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AroundActivity extends Fragment implements OnClickListener,
		OnItemClickListener {
	// 维度
	private double latitude;
	// 经度
	private double longgitude;
	private ListView around_lv;
	private RestaurantAdapter adapter;
	private LoadingImgView mLoadingImg;// 加载图片
	protected Button mReloadBtn;// 重新加载
	private TextView mLoadingText;// 加载文字
	private View mLoadingView;// 加载VIew
	private boolean mIsLoading = false;// 判断不在没有加载完内容的情况下 重新加载
	private LocationClient locationClient = null;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				around_lv.setAdapter(adapter);
				around_lv.setVisibility(View.VISIBLE);
			} else if (msg.what == 2) {
				loadFailue();
			} else if (msg.what == 3) {
				loadSuccess();
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_around, null);
		around_lv = (ListView) view.findViewById(R.id.around_lv);
		mLoadingText = (TextView) view.findViewById(R.id.loading_tv);
		mLoadingImg = (LoadingImgView) view.findViewById(R.id.loading_img);
		mReloadBtn = (Button) view.findViewById(R.id.loading_reload_btn);
		mLoadingView = view.findViewById(R.id.loading_layout);
		adapter = new RestaurantAdapter(new ArrayList<Restaurant>(),
				getActivity());
		around_lv.setAdapter(adapter);
		loading();// 加载view
		around_lv.setOnItemClickListener(this);
		mReloadBtn.setOnClickListener(this);
		locationClient = new LocationClient(getActivity()
				.getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setOpenGps(true); // 是否打开GPS
		option.setCoorType("bd09ll"); // 设置返回值的坐标类型
		option.setProdName("teachcooking"); // 设置产品线名称。
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		locationClient.setLocOption(option);
		// 注册位置监听器
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null) {
					loadFailue();
					return;
				}
				showLocation(location);
				loadData();// 加载方法
			}
		});
		locationClient.start();
		/**
		 * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。 调用requestLocation(
		 * )后，每隔设定的时间，定位SDK就会进行一次定位。 如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
		 * 返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
		 * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
		 */
		locationClient.requestLocation();
		// loadDate();// 加载方法
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
			locationClient = null;
		}
		mIsLoading = false;
	}

	private void showLocation(BDLocation location) {
		longgitude = location.getLongitude();
		latitude = location.getLatitude();
	}

	private void loadData() {
		if (mIsLoading) {
			return;
		}
		mIsLoading = true;
		new Thread(new Runnable() {

			@Override
			public void run() {
				String key = "36004b4ff610f0d6698c444bd3a62a46";
				String url = "http://apis.juhe.cn/catering/query?key=" + key
						+ "&lng=" + longgitude + "&lat=" + latitude
						+ "&radius=3000";
				HttpParams params = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(params, 8 * 1000);
				HttpConnectionParams.setSoTimeout(params, 8 * 1000);
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url.toString());
				BufferedReader br = null;
				HttpResponse httpResponse;
				try {
					httpResponse = httpClient.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						HttpEntity httpEntity = httpResponse.getEntity();
						InputStream is = httpEntity.getContent();
						br = new BufferedReader(new InputStreamReader(is));
						String reString = "";
						StringBuffer stringBuffer = new StringBuffer();
						while ((reString = br.readLine()) != null) {
							stringBuffer.append(reString);
						}
						br.close();
						System.out.println(stringBuffer.toString());
						if (stringBuffer != null) {
							handler.sendEmptyMessage(3);
							ResponseClass response = JSONObject.parseObject(
									stringBuffer.toString(),
									ResponseClass.class);
							System.out.println(response.getResultcode());

							List<Restaurant> list = response.getResult();
							List<Restaurant> list2 = new ArrayList<Restaurant>();
							for (Restaurant restaurant : list) {
								list2.add(restaurant);
							}
							adapter = new RestaurantAdapter(list2,
									getActivity());
							handler.sendEmptyMessage(1);
							// 可以进行下次的内容加载
							mIsLoading = false;
						} else {
							handler.sendEmptyMessage(2);
							return;
						}
					} else {
						handler.sendEmptyMessage(2);
						return;

					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						if (br != null) {
							br.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}).start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loading_reload_btn:
			loading();
			if (locationClient != null && locationClient.isStarted()) {
				locationClient.stop();
				locationClient = null;
			}
			locationClient = new LocationClient(getActivity()
					.getApplicationContext());
			locationClient.start();
			locationClient.requestLocation();
			break;

		}
	}

	public void loading() {
		mLoadingImg.loading();
		mReloadBtn.setVisibility(View.INVISIBLE);
		mLoadingText.setText(getResources().getString(R.string.loading));
	}

	protected void loadSuccess() {
		mLoadingImg.stopAnim();
		mLoadingView.setVisibility(View.GONE);
	}

	protected void loadFailue() {
		mLoadingImg.failed();
		mLoadingText.setText(getResources().getString(
				R.string.load_failed_please_retry));
		mReloadBtn.setVisibility(View.VISIBLE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Restaurant restaurant = adapter.getItem(position);
		Intent intent = new Intent(getActivity(),
				RestaurantDetailActivity.class);
		intent.putExtra("latitude", latitude);
		intent.putExtra("longgitude", longgitude);
		intent.putExtra("restaurant", restaurant);
		startActivity(intent);
	}
}

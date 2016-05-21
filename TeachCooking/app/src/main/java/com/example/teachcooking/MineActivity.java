package com.example.teachcooking;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.example.teachcooking.adpter.MyPublishCookBookAdapter;
import com.example.teachcooking.adpter.MyPublishProductAdapter;
import com.example.teachcooking.entity.CookBookInfo;
import com.example.teachcooking.entity.UsuerInfo;
import com.example.teachcooking.view.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MineActivity extends Fragment implements OnClickListener,
		OnItemClickListener {
	private Button login_now;
	private TextView mine_usuer_name;
	private ArrayList<RadioButton> arrayList = new ArrayList<RadioButton>();
	private RadioButton mine_production, mine_cookbook;
	private UsuerInfo usuerInfo;
	private CircleImageView mine_pic;
	private ImageView mine_setting;
	private ListView mine_listview;
	private MyPublishCookBookAdapter adapter;
	private List<CookBookInfo> cookBookInfos;
	private GridView gridView;
	private MyPublishProductAdapter adapter2;
	private TextView collection;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_mine, null);
		collection = (TextView) view.findViewById(R.id.collection);
		login_now = (Button) view.findViewById(R.id.login_now);
		mine_pic = new CircleImageView(getActivity());
		mine_pic = (CircleImageView) view.findViewById(R.id.mine_pic);
		mine_listview = (ListView) view.findViewById(R.id.mine_listview);
		gridView = (GridView) view.findViewById(R.id.mine_gridview);
		mine_usuer_name = (TextView) view.findViewById(R.id.mine_usuer_name);
		mine_setting = (ImageView) view.findViewById(R.id.mine_setting);
		mine_production = (RadioButton) view.findViewById(R.id.mine_production);
		mine_cookbook = (RadioButton) view.findViewById(R.id.mine_cookbook);
		mine_cookbook.setOnClickListener(this);
		mine_production.setOnClickListener(this);
		login_now.setOnClickListener(this);
		mine_setting.setOnClickListener(this);
		mine_listview.setOnItemClickListener(this);
		gridView.setOnItemClickListener(this);
		collection.setOnClickListener(this);
		mine_pic.setOnClickListener(this);
		arrayList.add(mine_production);
		arrayList.add(mine_cookbook);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		arrayList.get(0).setChecked(true);
		usuerInfo = BmobUser.getCurrentUser(getActivity(), UsuerInfo.class);
		if (usuerInfo == null) {
			login_now.setVisibility(View.VISIBLE);
			adapter.setCookBookInfos(new ArrayList<CookBookInfo>());
			adapter2.setCookBookInfos(new ArrayList<CookBookInfo>());
			mine_pic.setImageResource(R.drawable.portrait);
			mine_usuer_name.setText("");
		}else {
			getCurrentUser();			
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new MyPublishCookBookAdapter(getActivity(),
				new ArrayList<CookBookInfo>());
		adapter2 = new MyPublishProductAdapter(getActivity(),
				new ArrayList<CookBookInfo>());
		mine_listview.setAdapter(adapter);
		gridView.setAdapter(adapter2);
	}

	private void getCurrentUser() {
		if (usuerInfo != null) {
			usuerInfo.getPic().loadImage(getActivity(), mine_pic);
			login_now.setVisibility(View.GONE);
			mine_usuer_name.setText(usuerInfo.getNick());
			BmobQuery<CookBookInfo> query = new BmobQuery<CookBookInfo>();
			query.addWhereEqualTo("author", usuerInfo);
			query.order("-createdAt");
			query.include("author");
			query.findObjects(getActivity(), new FindListener<CookBookInfo>() {

				@Override
				public void onSuccess(List<CookBookInfo> object) {
					cookBookInfos = object;
					if (cookBookInfos != null) {
						mine_production.setText("作品" + object.size());
						mine_cookbook.setText("菜谱" + object.size());
						adapter.setCookBookInfos(cookBookInfos);
						adapter2.setCookBookInfos(cookBookInfos);
					}
				}

				@Override
				public void onError(int code, String msg) {
					Toast.makeText(getActivity(), "错误码：" + code + msg,
							Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			mine_pic.setImageResource(R.drawable.portrait);
			login_now.setVisibility(View.VISIBLE);
			mine_usuer_name.setText("你尚未登录");
			if (adapter.getCount() > 0 || adapter2.getCount() > 0) {
				adapter.setCookBookInfos(new ArrayList<CookBookInfo>());
				adapter2.setCookBookInfos(new ArrayList<CookBookInfo>());
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.collection:
			if (usuerInfo != null) {
				Intent intent_collection = new Intent(getActivity(),
						CollectActivity.class);
				startActivity(intent_collection);
			}
			break;
		case R.id.login_now:
			Intent intent = new Intent(getActivity(), UsuerActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_cookbook:
			arrayList.get(1).setChecked(true);
			arrayList.get(0).setChecked(false);
			mine_listview.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.GONE);
			break;
		case R.id.mine_production:
			arrayList.get(0).setChecked(true);
			arrayList.get(1).setChecked(false);
			mine_listview.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);
			break;
		case R.id.mine_setting:
			if (usuerInfo != null) {
				Intent intent2 = new Intent(getActivity(),
						SettingActivity.class);
				startActivity(intent2);
			}
			break;
			case R.id.mine_pic:
				if (usuerInfo != null) {
					Intent inten3 = new Intent(getActivity(), EditUsuerInfoActivity.class);
					startActivity(inten3);
				}
				break;
		}
	}

	@Override
	public void onPause() {
		arrayList.get(0).setChecked(true);
		arrayList.get(1).setChecked(false);
		mine_listview.setVisibility(View.GONE);
		gridView.setVisibility(View.VISIBLE);
		super.onPause();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent instanceof ListView) {
			CookBookInfo cookBookInfo = adapter.getItem(position);
			Intent intent = new Intent(getActivity(), TeachDetailActivity.class);
			intent.putExtra("cookBookInfo", cookBookInfo);
			startActivity(intent);
		}

	}
}

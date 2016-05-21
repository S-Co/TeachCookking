package com.example.teachcooking;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.example.teachcooking.adpter.MyPublishCookBookAdapter;
import com.example.teachcooking.adpter.MyPublishProductAdapter;
import com.example.teachcooking.entity.CookBookInfo;
import com.example.teachcooking.entity.UsuerInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class UserDetailActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private TextView usuer_production_buttom, usuer_cookbook_buttom;
	private TextView user_introduce;
	private TextView user_detail_join_time;
	private TextView user_detail_nick;
	private ImageView user_detail_pic;
	private ArrayList<RadioButton> radioButtons = new ArrayList<RadioButton>();
	private RadioButton user_production, user_cookbook;
	private ArrayList<TextView> textViews = new ArrayList<TextView>();
	private List<CookBookInfo> cookBookInfos;
	private MyPublishProductAdapter adapter2;
	private GridView gridView;
	private ListView listView;
	private MyPublishCookBookAdapter adapter;
	private UsuerInfo usuerInfo;
	private TextView user_sexandadress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 透明状态栏
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		setContentView(R.layout.activity_user_detail);
		usuerInfo = (UsuerInfo) getIntent().getSerializableExtra("usuerInfo");
		gridView = (GridView) findViewById(R.id.user_gridview);
		listView = (ListView) findViewById(R.id.user_listview);
		user_sexandadress = (TextView) findViewById(R.id.user_sexandadress);
		user_production = (RadioButton) findViewById(R.id.user_production);
		user_cookbook = (RadioButton) findViewById(R.id.user_cookbook);
		usuer_production_buttom = (TextView) findViewById(R.id.usuer_production_buttom);
		usuer_cookbook_buttom = (TextView) findViewById(R.id.usuer_cookbook_buttom);
		user_introduce = (TextView) findViewById(R.id.user_introduce);
		user_detail_join_time = (TextView) findViewById(R.id.user_detail_join_time);
		user_detail_nick = (TextView) findViewById(R.id.user_detail_nick);
		user_detail_pic = (ImageView) findViewById(R.id.user_detail_pic);
		user_cookbook.setOnClickListener(this);
		user_sexandadress.setOnClickListener(this);
		user_production.setOnClickListener(this);
		gridView.setOnItemClickListener(this);
		listView.setOnItemClickListener(this);
		radioButtons.add(user_production);
		radioButtons.add(user_cookbook);

		textViews.add(usuer_production_buttom);
		textViews.add(usuer_cookbook_buttom);
		adapter = new MyPublishCookBookAdapter(this, new ArrayList<CookBookInfo>());
		adapter2 = new MyPublishProductAdapter(this, new ArrayList<CookBookInfo>());
		gridView.setAdapter(adapter2);
		listView.setAdapter(adapter);
		if (usuerInfo != null) {
			usuerInfo.getPic().loadImage(this, user_detail_pic);
			user_detail_nick.setText(usuerInfo.getNick());
			user_introduce.setText(usuerInfo.getSelfIntroduction());
			user_detail_join_time.setText(usuerInfo.getCreatedAt());
			user_sexandadress.setText(usuerInfo.getSex()+" "+usuerInfo.getHometown());
			BmobQuery<CookBookInfo> query = new BmobQuery<CookBookInfo>();
			query.addWhereEqualTo("author", usuerInfo);
			query.order("-createdAt");
			query.include("author");
			query.findObjects(UserDetailActivity.this,
					new FindListener<CookBookInfo>() {

						@Override
						public void onSuccess(List<CookBookInfo> object) {
							cookBookInfos = object;
							if (cookBookInfos != null) {
								user_production.setText("作品" + object.size());
								user_cookbook.setText("菜谱" + object.size());
								adapter.setCookBookInfos(cookBookInfos);
								adapter2.setCookBookInfos(cookBookInfos);
							}
						}

						@Override
						public void onError(int code, String msg) {
							Toast.makeText(UserDetailActivity.this,
									"错误码：" + code + msg, Toast.LENGTH_SHORT)
									.show();
						}
					});
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_cookbook:
			radioButtons.get(1).setChecked(true);
			radioButtons.get(0).setChecked(false);
			textViews.get(0).setBackgroundColor(
					getResources().getColor(R.color.White));
			textViews.get(1).setBackgroundColor(
					getResources().getColor(R.color.Orange));
			listView.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.GONE);
			break;
		case R.id.user_production:
			radioButtons.get(0).setChecked(true);
			radioButtons.get(1).setChecked(false);
			textViews.get(1).setBackgroundColor(
					getResources().getColor(R.color.White));
			textViews.get(0).setBackgroundColor(
					getResources().getColor(R.color.Orange));
			listView.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent instanceof ListView) {
			CookBookInfo cookBookInfo = adapter.getItem(position);
			Intent intent = new Intent(this, TeachDetailActivity.class);
			intent.putExtra("cookBookInfo", cookBookInfo);
			startActivity(intent);
		} else {
//			CookBookInfo cookBookInfo = adapter.getItem(position);
//			Intent intent = new Intent(this, UserDetailActivity.class);
//			intent.putExtra("cookBookInfo", cookBookInfo);
//			startActivity(intent);
		}
	}
}
